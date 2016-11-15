package configurationeditor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.scene.paint.Color;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class ConfigurationTest {
    private static Configuration instance;
    private final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

    private static final File sourceVideo = new File("assets/race1.mp4");
    private static final File sourceTelemetry = new File("assets/race1/");

    private static final Double videoStartTime = 4.2;
    private static final Double videoEndTime = 191.4;
    private static final Double syncRacestart = 3.5;

    private static final File outputVideo = new File("outputs/race1.mp4");

    private static final String headingText = "Kart One UK Nationals";
    private static final String subheadingText = "Round 1 of 3 - Sprint Race - Glencairn East";
    private static final File headingFont = new File("assets/Roboto-Regular.ttf");
    private static final Integer headingFontSize = 30;
    private static final Color headingFontColor = Color.GOLD;
    private static final Color headingColor = Color.SILVER;
    private static final File seriesLogo = new File("assets/KartOneUKNationals.jpg");

    private static final File backdrop = new File("assets/Glencairn.jpg");
    private static final File logo = new File("assets/GlencairnLogo.png");
    private static final Integer logoHeight = 200;
    private static final Integer logoWidth = 150;

    private static final File font = new File("assets/Roboto-Regular.ttf");
    private static final Integer fontSize = 15;
    private static final Color fontColor = Color.FIREBRICK;

    private static final Integer margin = 20;
    private static final Integer columnMargin = 10;
    private static final Integer resultLines = 12;

    private static final Boolean showChampion = false;

    private static final List<PointStructureItem> pointStructure = Arrays.asList(
            new PointStructureItem(0, 5),
            new PointStructureItem(1, 10),
            new PointStructureItem(2, 9),
            new PointStructureItem(3, 8),
            new PointStructureItem(4, 7),
            new PointStructureItem(5, 6),
            new PointStructureItem(6, 5),
            new PointStructureItem(7, 4),
            new PointStructureItem(8, 3),
            new PointStructureItem(9, 2),
            new PointStructureItem(10, 1)
    );

    private static final List<Driver> participantConfiguration = Arrays.asList(
            new Driver("Kobernulf Monnur", "Monnur", "MON", new Car("125cc Shifter Kart", new CarClass("Kart 1", Color.rgb(255, 0, 0)))),
            new Driver("Ayrton Senna", "Senna", "SEN", new Car("Lotus 98T", new CarClass("Vintage F1 C", Color.rgb(0, 255, 0))))
    );

    @Before
    public void setUp() throws Exception {
        instance = new Configuration();
    }

    private static void updateConfiguration(JsonNode data, Configuration configuration) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.readerForUpdating(configuration).readValue(data);
    }

    @Test
    public void getSourceVideo() throws Exception {
        ObjectNode data = nodeFactory.objectNode().put("source_video", sourceVideo.getPath());
        updateConfiguration(data, instance);

        File expResult = sourceVideo;
        File result = instance.getSourceVideo();
        assertThat(result, equalTo(expResult));
    }

    @Test
    public void setSourceVideo() throws Exception {
        instance.setSourceVideo(mock(File.class));
    }

    @Test
    public void sourceVideoProperty() throws Exception {
        SimpleObjectProperty<File> expResult = new SimpleObjectProperty<>();
        SimpleObjectProperty<File> result = instance.sourceVideoProperty();
        assertThat(result, instanceOf(expResult.getClass()));
    }

    @Test
    public void getSourceTelemetry() throws Exception {
        ObjectNode data = nodeFactory.objectNode().put("source_telemetry", sourceTelemetry.getPath());
        updateConfiguration(data, instance);

        File expResult = sourceTelemetry;
        File result = instance.getSourceTelemetry();
        assertThat(result, equalTo(expResult));
    }

    @Test
    public void setSourceTelemetry() throws Exception {
        instance.setSourceTelemetry(mock(File.class));
    }

    @Test
    public void sourceTelemetryProperty() throws Exception {
        SimpleObjectProperty<File> expResult = new SimpleObjectProperty<>();
        SimpleObjectProperty<File> result = instance.sourceTelemetryProperty();
        assertThat(result, instanceOf(expResult.getClass()));
    }

    @Test
    public void getVideoStartTime() throws Exception {
        ObjectNode data = nodeFactory.objectNode().put("video_skipstart", videoStartTime);
        updateConfiguration(data, instance);

        Double expResult = videoStartTime;
        Double result = instance.getVideoStartTime();
        assertThat(result, equalTo(expResult));
    }

    @Test
    public void setVideoStartTime() throws Exception {
        instance.setVideoStartTime(2.5);
    }

    @Test
    public void videoStartTimeProperty() throws Exception {
        SimpleDoubleProperty expResult = new SimpleDoubleProperty();
        SimpleDoubleProperty result = instance.videoStartTimeProperty();
        assertThat(result, instanceOf(expResult.getClass()));
    }

    @Test
    public void getVideoEndTime() throws Exception {
        ObjectNode data = nodeFactory.objectNode().put("video_skipend", videoEndTime);
        updateConfiguration(data, instance);

        Double expResult = videoEndTime;
        Double result = instance.getVideoEndTime();
        assertThat(result, equalTo(expResult));
    }

    @Test
    public void setVideoEndTime() throws Exception {
        instance.setVideoEndTime(200.3);
    }

    @Test
    public void videoEndTimeProperty() throws Exception {
        SimpleDoubleProperty expResult = new SimpleDoubleProperty();
        SimpleDoubleProperty result = instance.videoEndTimeProperty();
        assertThat(result, instanceOf(expResult.getClass()));
    }

    @Test
    public void getSyncRacestart() throws Exception {
        ObjectNode data = nodeFactory.objectNode().put("sync_racestart", syncRacestart);
        updateConfiguration(data, instance);

        Double expResult = syncRacestart;
        Double result = instance.getSyncRacestart();
        assertThat(result, equalTo(expResult));
    }

    @Test
    public void setSyncRacestart() throws Exception {
        instance.setSyncRacestart(5.5);
    }

    @Test
    public void syncRacestartProperty() throws Exception {
        SimpleDoubleProperty expResult = new SimpleDoubleProperty();
        SimpleDoubleProperty result = instance.syncRacestartProperty();
        assertThat(result, instanceOf(expResult.getClass()));
    }

    @Test
    public void getOutputVideo() throws Exception {
        ObjectNode data = nodeFactory.objectNode().put("output_video", outputVideo.getPath());
        updateConfiguration(data, instance);

        File expResult = outputVideo;
        File result = instance.getOutputVideo();
        assertThat(result, equalTo(expResult));
    }

    @Test
    public void setOutputVideo() throws Exception {
        instance.setOutputVideo(mock(File.class));
    }

    @Test
    public void outputVideoProperty() throws Exception {
        SimpleObjectProperty<File> expResult = new SimpleObjectProperty<>();
        SimpleObjectProperty<File> result = instance.outputVideoProperty();
        assertThat(result, instanceOf(expResult.getClass()));
    }

    @Test
    public void getHeadingText() throws Exception {
        ObjectNode data = nodeFactory.objectNode().put("heading_text", headingText);
        updateConfiguration(data, instance);

        String expResult = headingText;
        String result = instance.getHeadingText();
        assertThat(result, equalTo(expResult));
    }

    @Test
    public void setHeadingText() throws Exception {
        instance.setHeadingText("New Heading Text");
    }

    @Test
    public void headingTextProperty() throws Exception {
        SimpleStringProperty expResult = new SimpleStringProperty();
        SimpleStringProperty result = instance.headingTextProperty();
        assertThat(result, instanceOf(expResult.getClass()));
    }

    @Test
    public void getSubheadingText() throws Exception {
        ObjectNode data = nodeFactory.objectNode().put("subheading_text", subheadingText);
        updateConfiguration(data, instance);

        String expResult = subheadingText;
        String result = instance.getSubheadingText();
        assertThat(result, equalTo(expResult));
    }

    @Test
    public void setSubheadingText() throws Exception {
        instance.setSubheadingText("new Subheading Text");
    }

    @Test
    public void subheadingTextProperty() throws Exception {
        SimpleStringProperty expResult = new SimpleStringProperty();
        SimpleStringProperty result = instance.subheadingTextProperty();
        assertThat(result, instanceOf(expResult.getClass()));
    }

    @Test
    public void getHeadingFont() throws Exception {
        ObjectNode data = nodeFactory.objectNode().put("heading_font", headingFont.getPath());
        updateConfiguration(data, instance);

        File expResult = headingFont;
        File result = instance.getHeadingFont();
        assertThat(result, equalTo(expResult));
    }

    @Test
    public void setHeadingFont() throws Exception {
        instance.setHeadingFont(mock(File.class));
    }

    @Test
    public void headingFontProperty() throws Exception {
        SimpleObjectProperty<File> expResult = new SimpleObjectProperty<>();
        SimpleObjectProperty<File> result = instance.headingFontProperty();
        assertThat(result, instanceOf(expResult.getClass()));
    }

    @Test
    public void getHeadingFontSize() throws Exception {
        ObjectNode data = nodeFactory.objectNode().put("heading_font_size", headingFontSize);
        updateConfiguration(data, instance);

        Integer expResult = headingFontSize;
        Integer result = instance.getHeadingFontSize();
        assertThat(result, equalTo(expResult));
    }

    @Test
    public void setHeadingFontSize() throws Exception {
        instance.setHeadingFontSize(25);
    }

    @Test
    public void headingFontSizeProperty() throws Exception {
        SimpleIntegerProperty expResult = new SimpleIntegerProperty();
        SimpleIntegerProperty result = instance.headingFontSizeProperty();
        assertThat(result, instanceOf(expResult.getClass()));
    }

    @Test
    public void getHeadingFontColor() throws Exception {
        ArrayNode colorData = nodeFactory.arrayNode(3);
        colorData.add((int) (headingFontColor.getRed() * 255));
        colorData.add((int) (headingFontColor.getGreen() * 255));
        colorData.add((int) (headingFontColor.getBlue() * 255));
        ObjectNode data = nodeFactory.objectNode();
        data.set("heading_font_color", colorData);
        updateConfiguration(data, instance);

        Color expResult = headingFontColor;
        Color result = instance.getHeadingFontColor();
        assertThat(result, equalTo(expResult));
    }

    @Test
    public void setHeadingFontColor() throws Exception {
//        instance.setHeadingFontColor(mock(Color.class));
        instance.setHeadingFontColor(Color.RED);
    }

    @Test
    public void headingFontColorProperty() throws Exception {
        SimpleObjectProperty<Color> expResult = new SimpleObjectProperty<>();
        SimpleObjectProperty<Color> result = instance.headingFontColorProperty();
        assertThat(result, instanceOf(expResult.getClass()));
    }

    @Test
    public void getHeadingColor() throws Exception {
        ArrayNode colorData = nodeFactory.arrayNode(3);
        colorData.add((int) (headingColor.getRed() * 255));
        colorData.add((int) (headingColor.getGreen() * 255));
        colorData.add((int) (headingColor.getBlue() * 255));
        ObjectNode data = nodeFactory.objectNode();
        data.set("heading_color", colorData);
        updateConfiguration(data, instance);

        Color expResult = headingColor;
        Color result = instance.getHeadingColor();
        assertThat(result, equalTo(expResult));
    }

    @Test
    public void setHeadingColor() throws Exception {
//        instance.setHeadingColor(mock(Color.class));
        instance.setHeadingColor(Color.BLUE);
    }

    @Test
    public void headingColorProperty() throws Exception {
        SimpleObjectProperty<Color> expResult = new SimpleObjectProperty<>();
        SimpleObjectProperty<Color> result = instance.headingColorProperty();
        assertThat(result, instanceOf(expResult.getClass()));
    }

    @Test
    public void getSeriesLogo() throws Exception {
        ObjectNode data = nodeFactory.objectNode().put("series_logo", seriesLogo.getPath());
        updateConfiguration(data, instance);

        File expResult = seriesLogo;
        File result = instance.getSeriesLogo();
        assertThat(result, equalTo(expResult));
    }

    @Test
    public void setSeriesLogo() throws Exception {
        instance.setSeriesLogo(mock(File.class));
    }

    @Test
    public void seriesLogoProperty() throws Exception {
        SimpleObjectProperty<File> expResult = new SimpleObjectProperty<>();
        SimpleObjectProperty<File> result = instance.seriesLogoProperty();
        assertThat(result, instanceOf(expResult.getClass()));
    }

    @Test
    public void getBackdrop() throws Exception {
        ObjectNode data = nodeFactory.objectNode().put("backdrop", backdrop.getPath());
        updateConfiguration(data, instance);

        File expResult = backdrop;
        File result = instance.getBackdrop();
        assertThat(result, equalTo(expResult));
    }

    @Test
    public void setBackdrop() throws Exception {
        instance.setBackdrop(mock(File.class));
    }

    @Test
    public void backdropProperty() throws Exception {
        SimpleObjectProperty<File> expResult = new SimpleObjectProperty<>();
        SimpleObjectProperty<File> result = instance.backdropProperty();
        assertThat(result, instanceOf(expResult.getClass()));
    }

    @Test
    public void getLogo() throws Exception {
        ObjectNode data = nodeFactory.objectNode().put("logo", logo.getPath());
        updateConfiguration(data, instance);

        File expResult = logo;
        File result = instance.getLogo();
        assertThat(result, equalTo(expResult));
    }

    @Test
    public void setLogo() throws Exception {
        instance.setLogo(mock(File.class));
    }

    @Test
    public void logoProperty() throws Exception {
        SimpleObjectProperty<File> expResult = new SimpleObjectProperty<>();
        SimpleObjectProperty<File> result = instance.logoProperty();
        assertThat(result, instanceOf(expResult.getClass()));
    }

    @Test
    public void getLogoHeight() throws Exception {
        ObjectNode data = nodeFactory.objectNode().put("logo_height", logoHeight);
        updateConfiguration(data, instance);

        Integer expResult = logoHeight;
        Integer result = instance.getLogoHeight();
        assertThat(result, equalTo(expResult));
    }

    @Test
    public void setLogoHeight() throws Exception {
        instance.setLogoHeight(50);
    }

    @Test
    public void logoHeightProperty() throws Exception {
        SimpleIntegerProperty expResult = new SimpleIntegerProperty();
        SimpleIntegerProperty result = instance.logoHeightProperty();
        assertThat(result, instanceOf(expResult.getClass()));
    }

    @Test
    public void getLogoWidth() throws Exception {
        ObjectNode data = nodeFactory.objectNode().put("logo_width", logoWidth);
        updateConfiguration(data, instance);

        Integer expResult = logoWidth;
        Integer result = instance.getLogoWidth();
        assertThat(result, equalTo(expResult));
    }

    @Test
    public void setLogoWidth() throws Exception {
        instance.setLogoWidth(50);
    }

    @Test
    public void logoWidthProperty() throws Exception {
        SimpleIntegerProperty expResult = new SimpleIntegerProperty();
        SimpleIntegerProperty result = instance.logoWidthProperty();
        assertThat(result, instanceOf(expResult.getClass()));
    }

    @Test
    public void getFont() throws Exception {
        ObjectNode data = nodeFactory.objectNode().put("font", font.getPath());
        updateConfiguration(data, instance);

        File expResult = font;
        File result = instance.getFont();
        assertThat(result, equalTo(expResult));
    }

    @Test
    public void setFont() throws Exception {
        instance.setFont(mock(File.class));
    }

    @Test
    public void fontProperty() throws Exception {
        SimpleObjectProperty<File> expResult = new SimpleObjectProperty<>();
        SimpleObjectProperty<File> result = instance.fontProperty();
        assertThat(result, instanceOf(expResult.getClass()));
    }

    @Test
    public void getFontSize() throws Exception {
        ObjectNode data = nodeFactory.objectNode().put("font_size", fontSize);
        updateConfiguration(data, instance);

        Integer expResult = fontSize;
        Integer result = instance.getFontSize();
        assertThat(result, equalTo(expResult));
    }

    @Test
    public void setFontSize() throws Exception {
        instance.setFontSize(50);
    }

    @Test
    public void fontSizeProperty() throws Exception {
        SimpleIntegerProperty expResult = new SimpleIntegerProperty();
        SimpleIntegerProperty result = instance.fontSizeProperty();
        assertThat(result, instanceOf(expResult.getClass()));
    }

    @Test
    public void getFontColor() throws Exception {
        ArrayNode colorData = nodeFactory.arrayNode(3);
        colorData.add((int) (fontColor.getRed() * 255));
        colorData.add((int) (fontColor.getGreen() * 255));
        colorData.add((int) (fontColor.getBlue() * 255));
        ObjectNode data = nodeFactory.objectNode();
        data.set("font_color", colorData);
        updateConfiguration(data, instance);

        Color expResult = fontColor;
        Color result = instance.getFontColor();
        assertThat(result, equalTo(expResult));
    }

    @Test
    public void setFontColor() throws Exception {
//        instance.setFontColor(mock(Color.class));
        instance.setFontColor(Color.BLUE);
    }

    @Test
    public void fontColorProperty() throws Exception {
        SimpleObjectProperty<Color> expResult = new SimpleObjectProperty<>();
        SimpleObjectProperty<Color> result = instance.fontColorProperty();
        assertThat(result, instanceOf(expResult.getClass()));
    }

    @Test
    public void getMargin() throws Exception {
        ObjectNode data = nodeFactory.objectNode().put("margin", margin);
        updateConfiguration(data, instance);

        Integer expResult = margin;
        Integer result = instance.getMargin();
        assertThat(result, equalTo(expResult));
    }

    @Test
    public void setMargin() throws Exception {
        instance.setMargin(50);
    }

    @Test
    public void marginProperty() throws Exception {
        SimpleIntegerProperty expResult = new SimpleIntegerProperty();
        SimpleIntegerProperty result = instance.marginProperty();
        assertThat(result, instanceOf(expResult.getClass()));
    }

    @Test
    public void getColumnMargin() throws Exception {
        ObjectNode data = nodeFactory.objectNode().put("column_margin", columnMargin);
        updateConfiguration(data, instance);

        Integer expResult = columnMargin;
        Integer result = instance.getColumnMargin();
        assertThat(result, equalTo(expResult));
    }

    @Test
    public void setColumnMargin() throws Exception {
        instance.setColumnMargin(50);
    }

    @Test
    public void columnMarginProperty() throws Exception {
        SimpleIntegerProperty expResult = new SimpleIntegerProperty();
        SimpleIntegerProperty result = instance.columnMarginProperty();
        assertThat(result, instanceOf(expResult.getClass()));
    }

    @Test
    public void getResultLines() throws Exception {
        ObjectNode data = nodeFactory.objectNode().put("result_lines", resultLines);
        updateConfiguration(data, instance);

        Integer expResult = resultLines;
        Integer result = instance.getResultLines();
        assertThat(result, equalTo(expResult));
    }

    @Test
    public void setResultLines() throws Exception {
        instance.setResultLines(10);
    }

    @Test
    public void resultLinesProperty() throws Exception {
        SimpleIntegerProperty expResult = new SimpleIntegerProperty();
        SimpleIntegerProperty result = instance.resultLinesProperty();
        assertThat(result, instanceOf(expResult.getClass()));
    }

    @Test
    public void isShowChampion() throws Exception {
        ObjectNode data = nodeFactory.objectNode().put("show_champion", showChampion);
        updateConfiguration(data, instance);

        Boolean expResult = showChampion;
        Boolean result = instance.isShowChampion();
        assertThat(result, equalTo(expResult));
    }

    @Test
    public void setShowChampion() throws Exception {
        instance.setShowChampion(true);
    }

    @Test
    public void showChampionProperty() throws Exception {
        SimpleBooleanProperty expResult = new SimpleBooleanProperty();
        SimpleBooleanProperty result = instance.showChampionProperty();
        assertThat(result, instanceOf(expResult.getClass()));
    }

    @Test
    public void getPointStructure() throws Exception {
        ObjectNode data = nodeFactory.objectNode();
        ArrayNode pointData = nodeFactory.arrayNode();
        pointStructure.forEach(item -> pointData.add(item.getPoints()));
        data.set("point_structure", pointData);
        updateConfiguration(data, instance);

        List<PointStructureItem> expResult = pointStructure;
        List<PointStructureItem> result = instance.getPointStructure();
        assertThat(result.stream().map(PointStructureItem::getFinishPosition).collect(Collectors.toList()),
                IsIterableContainingInOrder.contains(expResult.stream().map(PointStructureItem::getFinishPosition).toArray()));
        assertThat(result.stream().map(PointStructureItem::getPoints).collect(Collectors.toList()),
                IsIterableContainingInOrder.contains(expResult.stream().map(PointStructureItem::getPoints).toArray()));
    }

    @Test
    public void setPointStructure() throws Exception {
        instance.setPointStructure(FXCollections.emptyObservableList());
    }

    @Test
    public void pointStructureProperty() throws Exception {
        SimpleListProperty<PointStructureItem> expResult = new SimpleListProperty<>();
        SimpleListProperty<PointStructureItem> result = instance.pointStructureProperty();
        assertThat(result, instanceOf(expResult.getClass()));
    }

    @Test
    public void getParticipantConfiguration() throws Exception {
        ObjectNode data = nodeFactory.objectNode();
        ObjectNode driverData = nodeFactory.objectNode();
        for (Driver driver : participantConfiguration) {
            ObjectNode driverNode = nodeFactory.objectNode();
            driverNode.put("display", driver.getName());
            driverNode.put("short_display", "");
            driverNode.put("car", "");
            driverNode.put("team", "");
            driverNode.put("points", 0);
            driverData.set(driver.getName(), driverNode);
        }
        data.set("participant_config", driverData);
        updateConfiguration(data, instance);

        List<Driver> expResult = participantConfiguration;
        List<Driver> result = instance.getParticipantConfiguration();
        assertThat(result.stream().map(Driver::getName).collect(Collectors.toList()),
                IsIterableContainingInAnyOrder.containsInAnyOrder(expResult.stream().map(Driver::getName).toArray()));
    }

    @Test
    public void setParticipantConfiguration() throws Exception {
        instance.setParticipantConfiguration(FXCollections.emptyObservableList());
    }

    @Test
    public void participantConfigurationProperty() throws Exception {
        SimpleListProperty<Driver> expResult = new SimpleListProperty<>();
        SimpleListProperty<Driver> result = instance.participantConfigurationProperty();
        assertThat(result, instanceOf(expResult.getClass()));
    }

    @Test
    public void getCars() throws Exception {
        ObjectNode data = nodeFactory.objectNode();
        ObjectNode driverData = nodeFactory.objectNode();
        for (Driver driver : participantConfiguration) {
            ObjectNode driverNode = nodeFactory.objectNode();
            driverNode.put("display", driver.getName());
            driverNode.put("short_display", "");
            driverNode.put("car", driver.getCar().getCarName());
            driverNode.put("team", "");
            driverNode.put("points", 0);
            driverData.set(driver.getName(), driverNode);
        }
        data.set("participant_config", driverData);
        updateConfiguration(data, instance);

        List<Car> expResult = new ArrayList<Car>(
                participantConfiguration
                        .stream()
                        .map(Driver::getCar)
                        .collect(Collectors.toSet())
        );
        List<Car> result = instance.getCars();
        assertThat(result.stream().map(Car::getCarName).collect(Collectors.toList()),
                IsIterableContainingInAnyOrder.containsInAnyOrder(expResult.stream().map(Car::getCarName).toArray()));
        // TODO: 11/14/2016 Readd class testing once car class parsing is added.
//        assertThat(result.stream().map(Car::getCarClass).collect(Collectors.toList()),
//                IsIterableContainingInAnyOrder.containsInAnyOrder(expResult.stream().map(Car::getCarClass).map(carClass -> samePropertyValuesAs(carClass)).toArray()));
    }

    @Test
    public void setCars() throws Exception {
        instance.setCars(FXCollections.emptyObservableList());
    }

    @Test
    public void carsProperty() throws Exception {
        SimpleListProperty<Car> expResult = new SimpleListProperty<>();
        SimpleListProperty<Car> result = instance.carsProperty();
        assertThat(result, instanceOf(expResult.getClass()));
    }
}