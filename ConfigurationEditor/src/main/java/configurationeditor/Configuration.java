package configurationeditor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@JsonPropertyOrder(alphabetic = true)
// TODO: 10/20/2016 Add functionality for deprecation of old, used properties. For now, here.
@JsonIgnoreProperties(value = {"additional_participant_config", "video_cache", "video_gaptime", "video_threshold"})
public class Configuration {
    // Source Data
    @JsonProperty(value = "source_video")
    private final SimpleObjectProperty<File> sourceVideo;

    @JsonProperty(value = "source_telemetry")
    private final SimpleObjectProperty<File> sourceTelemetry;

    // Source Parameters
    @JsonProperty(value = "video_skipstart")
    private final SimpleDoubleProperty videoStartTime;

    @JsonProperty(value = "video_skipend")
    private final SimpleDoubleProperty videoEndTime;

    @JsonProperty(value = "sync_racestart")
    private final SimpleDoubleProperty syncRacestart;

    // Output
    @JsonProperty(value = "output_video")
    private final SimpleObjectProperty<File> outputVideo;

    //Headings
    @JsonProperty(value = "heading_text")
    private final SimpleStringProperty headingText;

    @JsonProperty(value = "subheading_text")
    private final SimpleStringProperty subheadingText;

    @JsonProperty(value = "heading_font")
    private final SimpleObjectProperty<File> headingFont;

    @JsonProperty(value = "heading_font_size")
    private final SimpleIntegerProperty headingFontSize;

    @JsonDeserialize(using = Configuration.ColorDeserializer.class)
    @JsonProperty(value = "heading_font_color")
    @JsonSerialize(using = Configuration.ColorSerializer.class)
    private final SimpleObjectProperty<Color> headingFontColor;

    @JsonDeserialize(using = Configuration.ColorDeserializer.class)
    @JsonProperty(value = "heading_color")
    @JsonSerialize(using = Configuration.ColorSerializer.class)
    private final SimpleObjectProperty<Color> headingColor;

    @JsonProperty(value = "series_logo")
    private final SimpleObjectProperty<File> seriesLogo;

    // Backgrounds
    @JsonProperty(value = "backdrop")
    private final SimpleObjectProperty<File> backdrop;

    @JsonProperty(value = "logo")
    private final SimpleObjectProperty<File> logo;

    @JsonProperty(value = "logo_height")
    private final SimpleIntegerProperty logoHeight;

    @JsonProperty(value = "logo_width")
    private final SimpleIntegerProperty logoWidth;

    // Font
    @JsonProperty(value = "font")
    private final SimpleObjectProperty<File> font;

    @JsonProperty(value = "font_size")
    private final SimpleIntegerProperty fontSize;

    @JsonDeserialize(using = Configuration.ColorDeserializer.class)
    @JsonProperty(value = "font_color")
    @JsonSerialize(using = Configuration.ColorSerializer.class)
    private final SimpleObjectProperty<Color> fontColor;

    // Layout
    @JsonProperty(value = "margin")
    private final SimpleIntegerProperty margin;

    @JsonProperty(value = "column_margin")
    private final SimpleIntegerProperty columnMargin;

    @JsonProperty(value = "result_lines")
    private final SimpleIntegerProperty resultLines;

    // Options
    @JsonProperty(value = "show_champion")
    private final SimpleBooleanProperty showChampion;

    @JsonDeserialize(using = Configuration.PointStructureDeserializer.class)
    @JsonProperty(value = "point_structure")
    @JsonSerialize(using = Configuration.PointStructureSerializer.class)
    private final SimpleListProperty<PointStructureItem> pointStructure;

    @JsonDeserialize(using = Configuration.ParticipantConfigurationDeserializer.class)
    @JsonProperty(value = "participant_config")
    @JsonSerialize(using = Configuration.ParticipantConfigurationSerializer.class)
    private final SimpleListProperty<Driver> participantConfiguration;

    @JsonIgnore
    private final SimpleListProperty<Car> cars;

    public Configuration() {
        // Source Data
        this.sourceVideo = new SimpleObjectProperty<>();
        this.sourceTelemetry = new SimpleObjectProperty<>();

        // Source Parameters
        this.videoStartTime = new SimpleDoubleProperty(0.0);
        this.videoEndTime = new SimpleDoubleProperty(0.0);
        this.syncRacestart = new SimpleDoubleProperty(0.0);

        // Output
        this.outputVideo = new SimpleObjectProperty<>();

        // Headings
        this.headingText = new SimpleStringProperty();
        this.subheadingText = new SimpleStringProperty();
        this.headingFont = new SimpleObjectProperty<>();
        this.headingFontSize = new SimpleIntegerProperty(20);
        this.headingFontColor = new SimpleObjectProperty<>(Color.rgb(255, 255, 255));
        this.headingColor = new SimpleObjectProperty<>(Color.rgb(255, 0, 0));
        this.seriesLogo = new SimpleObjectProperty<>();

        // Backgrounds
        this.backdrop = new SimpleObjectProperty<>();
        this.logo = new SimpleObjectProperty<>();
        this.logoHeight = new SimpleIntegerProperty(150);
        this.logoWidth = new SimpleIntegerProperty(150);

        // Font
        this.font = new SimpleObjectProperty<>();
        this.fontSize = new SimpleIntegerProperty(15);
        this.fontColor = new SimpleObjectProperty<>(Color.rgb(0, 0, 0));

        // Layout
        this.margin = new SimpleIntegerProperty(20);
        this.columnMargin = new SimpleIntegerProperty(10);
        this.resultLines = new SimpleIntegerProperty(10);

        // Options
        this.showChampion = new SimpleBooleanProperty(false);

        List<Integer> defaultPoints = new ArrayList<>(Arrays.asList(5, 25, 18, 15, 12, 10, 8, 6, 4, 2, 1));
        ObservableList<PointStructureItem> defaultPointStructure = FXCollections.observableArrayList();
        Integer index = 0;
        for (Integer points : defaultPoints) {
            defaultPointStructure.add(new PointStructureItem(index, points));
            index += 1;
        }
        this.pointStructure = new SimpleListProperty<>(defaultPointStructure);

        this.participantConfiguration = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<Driver>(), param -> new Observable[]{param.getCar().carNameProperty()}));
        this.cars = new SimpleListProperty<>(FXCollections.observableArrayList(new TreeSet<Car>()));
        this.participantConfiguration.addListener((observable, oldValue, newValue) -> cars.set(FXCollections.observableArrayList(newValue
                .stream()
                .map(Driver::getCar)
                .collect(Collectors.toSet()))));
    }

    public File getSourceVideo() {
        return sourceVideo.get();
    }

    public SimpleObjectProperty<File> sourceVideoProperty() {
        return sourceVideo;
    }

    public void setSourceVideo(File sourceVideo) {
        this.sourceVideo.set(sourceVideo);
    }

    public File getSourceTelemetry() {
        return sourceTelemetry.get();
    }

    public SimpleObjectProperty<File> sourceTelemetryProperty() {
        return sourceTelemetry;
    }

    public void setSourceTelemetry(File sourceTelemetry) {
        this.sourceTelemetry.set(sourceTelemetry);
    }

    public double getVideoStartTime() {
        return videoStartTime.get();
    }

    public SimpleDoubleProperty videoStartTimeProperty() {
        return videoStartTime;
    }

    public void setVideoStartTime(double videoStartTime) {
        this.videoStartTime.set(videoStartTime);
    }

    public double getVideoEndTime() {
        return videoEndTime.get();
    }

    public SimpleDoubleProperty videoEndTimeProperty() {
        return videoEndTime;
    }

    public void setVideoEndTime(double videoEndTime) {
        this.videoEndTime.set(videoEndTime);
    }

    public double getSyncRacestart() {
        return syncRacestart.get();
    }

    public SimpleDoubleProperty syncRacestartProperty() {
        return syncRacestart;
    }

    public void setSyncRacestart(double syncRacestart) {
        this.syncRacestart.set(syncRacestart);
    }

    public File getOutputVideo() {
        return outputVideo.get();
    }

    public SimpleObjectProperty<File> outputVideoProperty() {
        return outputVideo;
    }

    public void setOutputVideo(File outputVideo) {
        this.outputVideo.set(outputVideo);
    }

    public String getHeadingText() {
        return headingText.get();
    }

    public SimpleStringProperty headingTextProperty() {
        return headingText;
    }

    public void setHeadingText(String headingText) {
        this.headingText.set(headingText);
    }

    public String getSubheadingText() {
        return subheadingText.get();
    }

    public SimpleStringProperty subheadingTextProperty() {
        return subheadingText;
    }

    public void setSubheadingText(String subheadingText) {
        this.subheadingText.set(subheadingText);
    }

    public File getHeadingFont() {
        return headingFont.get();
    }

    public SimpleObjectProperty<File> headingFontProperty() {
        return headingFont;
    }

    public void setHeadingFont(File headingFont) {
        this.headingFont.set(headingFont);
    }

    public int getHeadingFontSize() {
        return headingFontSize.get();
    }

    public SimpleIntegerProperty headingFontSizeProperty() {
        return headingFontSize;
    }

    public void setHeadingFontSize(int headingFontSize) {
        this.headingFontSize.set(headingFontSize);
    }

    public Color getHeadingFontColor() {
        return headingFontColor.get();
    }

    public SimpleObjectProperty<Color> headingFontColorProperty() {
        return headingFontColor;
    }

    public void setHeadingFontColor(Color headingFontColor) {
        this.headingFontColor.set(headingFontColor);
    }

    public Color getHeadingColor() {
        return headingColor.get();
    }

    public SimpleObjectProperty<Color> headingColorProperty() {
        return headingColor;
    }

    public void setHeadingColor(Color headingColor) {
        this.headingColor.set(headingColor);
    }

    public File getSeriesLogo() {
        return seriesLogo.get();
    }

    public SimpleObjectProperty<File> seriesLogoProperty() {
        return seriesLogo;
    }

    public void setSeriesLogo(File seriesLogo) {
        this.seriesLogo.set(seriesLogo);
    }

    public File getBackdrop() {
        return backdrop.get();
    }

    public SimpleObjectProperty<File> backdropProperty() {
        return backdrop;
    }

    public void setBackdrop(File backdrop) {
        this.backdrop.set(backdrop);
    }

    public File getLogo() {
        return logo.get();
    }

    public SimpleObjectProperty<File> logoProperty() {
        return logo;
    }

    public void setLogo(File logo) {
        this.logo.set(logo);
    }

    public int getLogoHeight() {
        return logoHeight.get();
    }

    public SimpleIntegerProperty logoHeightProperty() {
        return logoHeight;
    }

    public void setLogoHeight(int logoHeight) {
        this.logoHeight.set(logoHeight);
    }

    public int getLogoWidth() {
        return logoWidth.get();
    }

    public SimpleIntegerProperty logoWidthProperty() {
        return logoWidth;
    }

    public void setLogoWidth(int logoWidth) {
        this.logoWidth.set(logoWidth);
    }

    public File getFont() {
        return font.get();
    }

    public SimpleObjectProperty<File> fontProperty() {
        return font;
    }

    public void setFont(File font) {
        this.font.set(font);
    }

    public int getFontSize() {
        return fontSize.get();
    }

    public SimpleIntegerProperty fontSizeProperty() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize.set(fontSize);
    }

    public Color getFontColor() {
        return fontColor.get();
    }

    public SimpleObjectProperty<Color> fontColorProperty() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor.set(fontColor);
    }

    public int getMargin() {
        return margin.get();
    }

    public SimpleIntegerProperty marginProperty() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin.set(margin);
    }

    public int getColumnMargin() {
        return columnMargin.get();
    }

    public SimpleIntegerProperty columnMarginProperty() {
        return columnMargin;
    }

    public void setColumnMargin(int columnMargin) {
        this.columnMargin.set(columnMargin);
    }

    public int getResultLines() {
        return resultLines.get();
    }

    public SimpleIntegerProperty resultLinesProperty() {
        return resultLines;
    }

    public void setResultLines(int resultLines) {
        this.resultLines.set(resultLines);
    }

    public boolean isShowChampion() {
        return showChampion.get();
    }

    public SimpleBooleanProperty showChampionProperty() {
        return showChampion;
    }

    public void setShowChampion(boolean showChampion) {
        this.showChampion.set(showChampion);
    }

    public ObservableList<PointStructureItem> getPointStructure() {
        return pointStructure.get();
    }

    public SimpleListProperty<PointStructureItem> pointStructureProperty() {
        return pointStructure;
    }

    public void setPointStructure(ObservableList<PointStructureItem> pointStructure) {
        this.pointStructure.set(pointStructure);
    }

    public ObservableList<Driver> getParticipantConfiguration() {
        return participantConfiguration.get();
    }

    public SimpleListProperty<Driver> participantConfigurationProperty() {
        return participantConfiguration;
    }

    public void setParticipantConfiguration(ObservableList<Driver> participantConfiguration) {
        this.participantConfiguration.set(participantConfiguration);
    }

    public ObservableList<Car> getCars() {
        return cars.get();
    }

    public SimpleListProperty<Car> carsProperty() {
        return cars;
    }

    public void setCars(ObservableList<Car> cars) {
        this.cars.set(cars);
    }

    private static class ColorDeserializer extends StdDeserializer<Color> {
        public ColorDeserializer() {
            this(null);
        }

        public ColorDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Color deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            ArrayList<Integer> color_values = new ArrayList<>();
            for (final JsonNode entry : node) {
                color_values.add(entry.asInt());
            }
            return Color.rgb(color_values.get(0), color_values.get(1), color_values.get(2));
        }
    }

    private static class ColorSerializer extends StdSerializer<Color> {
        public ColorSerializer() {
            this(null);
        }

        public ColorSerializer(Class<Color> t) {
            super(t);
        }

        @Override
        public void serialize(Color value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            int[] color_values = new int[3];
            color_values[0] = (int) (value.getRed() * 255);
            color_values[1] = (int) (value.getGreen() * 255);
            color_values[2] = (int) (value.getBlue() * 255);
            gen.writeArray(color_values, 0, 3);
        }
    }

    private static class PointStructureDeserializer extends StdDeserializer<ObservableList<PointStructureItem>> {
        public PointStructureDeserializer() {
            this(null);
        }

        public PointStructureDeserializer(Class vc) {
            super(vc);
        }

        @Override
        public ObservableList<PointStructureItem> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            ObservableList<PointStructureItem> pointStructure = FXCollections.observableArrayList();
            Integer index = 0;
            for (final JsonNode entry : node) {
                pointStructure.add(new PointStructureItem(index, entry.asInt()));
                index += 1;
            }
            return pointStructure;
        }
    }

    private static class PointStructureSerializer extends StdSerializer<ObservableList<PointStructureItem>> {
        public PointStructureSerializer() {
            this(null);
        }

        public PointStructureSerializer(Class<ObservableList<PointStructureItem>> t) {
            super(t);
        }

        @Override
        public void serialize(ObservableList<PointStructureItem> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            int[] pointStructure = new int[value.size()];
            Integer index = 0;
            for (PointStructureItem entry : value) {
                pointStructure[index] = entry.getPoints();
                index += 1;
            }
            gen.writeArray(pointStructure, 0, pointStructure.length);
        }
    }

    private static class ParticipantConfigurationDeserializer extends StdDeserializer<ObservableList<Driver>> {
        public ParticipantConfigurationDeserializer() {
            this(null);
        }

        public ParticipantConfigurationDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public ObservableList<Driver> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            ObservableList<Driver> drivers = FXCollections.observableList(new ArrayList<>(), param -> new Observable[]{param.displayNameProperty(), param.getCar().carNameProperty()});

            Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonNode> entry = iterator.next();
                Driver driver = new Driver(
                        entry.getKey(),
                        entry.getValue().findValue("display").textValue(),
                        entry.getValue().findValue("short_display").textValue(),
                        new Car(entry.getValue().findValue("car").textValue(), new CarClass("", Color.rgb(255, 0, 0))),
                        entry.getValue().findValue("team").textValue(),
                        entry.getValue().findValue("points").intValue()
                );
                drivers.add(driver);
            }

            return drivers;
        }
    }

    private static class ParticipantConfigurationSerializer extends StdSerializer<ObservableList<Driver>> {
        public ParticipantConfigurationSerializer() {
            this(null);
        }

        public ParticipantConfigurationSerializer(Class<ObservableList<Driver>> t) {
            super(t);
        }

        @Override
        public void serialize(ObservableList<Driver> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();
            for (Driver driver : value.sorted(Comparator.comparing(Driver::getName))) {
                gen.writeFieldName(driver.getName());
                gen.writeStartObject();
                gen.writeStringField("display", driver.getDisplayName());
                gen.writeStringField("short_display", driver.getShortName());
                if (driver.getCar() == null) {
                    gen.writeStringField("car", null);
                } else {
                    gen.writeStringField("car", driver.getCar().getCarName());
                }
                gen.writeStringField("team", driver.getTeam());
                gen.writeNumberField("points", driver.getSeriesPoints());
                gen.writeEndObject();
            }
            gen.writeEndObject();
        }
    }
}