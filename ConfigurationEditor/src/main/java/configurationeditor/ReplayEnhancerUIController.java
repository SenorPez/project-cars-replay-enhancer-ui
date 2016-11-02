package configurationeditor;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ReplayEnhancerUIController implements Initializable {
    private final SimpleObjectProperty<File> JSONFile = new SimpleObjectProperty<>();
    private Configuration configuration;

    @FXML
    private VBox root;
    
    @FXML
    private Tab tabDrivers;
    
    @FXML
    private TextField txtSourceVideo;
    
    @FXML
    private TextField txtSourceTelemetry;
    
    @FXML
    private TextField txtVideoStart;
    
    @FXML
    private TextField txtVideoEnd;
    
    @FXML
    private TextField txtVideoSync;
    
    @FXML
    private TextField txtOutputVideo;
    
    @FXML
    private TextField txtHeadingFont;
    
    @FXML
    private TextField txtHeadingFontSize;
    
    @FXML
    private ColorPicker colorHeadingFontColor;
    
    @FXML
    private TextField txtFont;
    
    @FXML
    private TextField txtFontSize;
    
    @FXML
    private ColorPicker colorFontColor;
    
    @FXML
    private TextField txtMarginWidth;
    
    @FXML
    private TextField txtColumnMarginWidth;
    
    @FXML
    private TextField txtResultLines;
    
    @FXML
    private TextField txtBackdrop;
        
    @FXML
    private TextField txtLogo;
        
    @FXML
    private TextField txtLogoWidth;
        
    @FXML
    private TextField txtLogoHeight;
    
    @FXML
    private ColorPicker colorHeadingColor;
    
    @FXML
    private TextField txtHeadingLogo;
    
    @FXML
    private TextField txtHeadingText;
    
    @FXML
    private TextField txtSubheadingText;

    @FXML
    private CheckBox cbShowChampion;

    @FXML
    private TextField txtBonusPoints;
    
    @FXML
    private TableView<PointStructureItem> tblPointStructure;
    
    @FXML
    private TableColumn<PointStructureItem, Integer> colFinishPosition;
    
    @FXML
    private TableColumn<PointStructureItem, Integer> colPoints;

    @FXML
    private TableView<Driver> tblDrivers;
    
    @FXML
    private TableColumn<Driver, String> colName;
    
    @FXML
    private TableColumn<Driver, String> colDisplayName;
    
    @FXML
    private TableColumn<Driver, String> colShortName;
    
    @FXML
    private TableColumn<Driver, String> colCar;
    
    @FXML
    private TableColumn<Driver, String> colTeam;
    
    @FXML
    private TableColumn<Driver, Integer> colSeriesPoints;

    @FXML
    private TableView<Car> tblCars;

    @FXML
    private TableColumn<Car, String> colCarName;

    @FXML
    private TableColumn<Car, String> colClassName;

    @FXML
    private TableColumn<Car, Color> colClassColor;
    
    @FXML
    private Label txtFileName;

    private static void updateConfiguration(File file, Configuration configuration) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String data = Files.lines(file.toPath()).collect(Collectors.joining());
        mapper.readerForUpdating(configuration).readValue(data);
    }
    
    private static File chooseJSONFile(Pane root) {
        Stage stage = (Stage) root.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Configuration File");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("JSON", "*.json"),
            new FileChooser.ExtensionFilter("All Files", "*.*"));

        return fileChooser.showOpenDialog(stage);
    }
    
    @FXML
    private void menuFileNew() {
        configuration = new Configuration();
        addListeners();
        JSONFile.set(null);
    }

    @FXML
    private void menuFileNewFrom() throws IOException {
        File file = chooseJSONFile(root);
        if (file != null && file.isFile()) {
            menuFileNew();
            updateConfiguration(file, configuration);
        }
    }
    
    @FXML
    private void menuFileOpen() throws IOException {
        File file = chooseJSONFile(root);
        if (file != null && file.isFile()) {
            menuFileNew();
            updateConfiguration(file, configuration);
            JSONFile.set(file);

            // TODO: 10/28/2016 Figure out why we need to do this for this sequence: Open -> Select Tele -> Open. Without, no Driver refresh.
            tblDrivers.refresh();
        }
    }

    @FXML
    private void menuFileSave() throws IOException {
        if (JSONFile.get() == null) {
            menuFileSaveAs();
        } else {
            writeJSONFile(JSONFile.get(), configuration);
        }
    }
    
    private void writeJSONFile(File file, Configuration configuration) throws IOException {
        if (file != null) {
            ObjectMapper mapper = new ObjectMapper();

            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));
            writer.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(configuration));
            writer.close();
        }
    }
    
    @FXML
    private void menuFileSaveAs() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Configuration File As");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON", "*.json"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        JSONFile.set(fileChooser.showSaveDialog(stage));

        writeJSONFile(JSONFile.get(), configuration);
    }

    @FXML
    private void menuFileExit() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void validateInteger(KeyEvent event) {
        Object source = event.getSource();
        TextField txtSource = (TextField) source;
        txtSource.setStyle("-fx-text-inner-color: black");

        try {
            Integer value = Integer.valueOf(txtSource.getText());
        } catch (NumberFormatException e) {
            if (!txtSource.getText().equals("")) {
                txtSource.setStyle("-fx-text-inner-color: red");
            }
        }
    }
    
    @FXML
    private void validateTime(KeyEvent event) {
        Object source = event.getSource();
        TextField txtSource = (TextField) source;
        txtSource.setStyle("-fx-text-inner-color: black");

        Pattern regex = Pattern.compile("(?:^(\\d*):([0-5]?\\d):([0-5]?\\d(?:\\.\\d*)?)$|^(\\d*):([0-5]?\\d(?:\\.\\d*)?)$|^(\\d*(?:\\.\\d*)?)$)");
        Matcher match = regex.matcher(txtSource.getText());
        if (!match.matches()) {
            txtSource.setStyle("-fx-text-inner-color: red");
        }
    }

    @FXML
    private void buttonSourceVideo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Source Video File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP4", "*.mp4"),
                new FileChooser.ExtensionFilter("AVI", "*.avi"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File file = fileChooser.showOpenDialog(root.getScene().getWindow());

        if (file != null && file.isFile()) {
            configuration.setSourceVideo(file);
        }
    }

    @FXML
    private void buttonSourceTelemetry(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open Source Telemetry Directory");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File directory = directoryChooser.showDialog(root.getScene().getWindow());

        if (directory != null && directory.isDirectory()) {
            configuration.setSourceTelemetry(directory);
            populateDrivers();
        }
    }

    @FXML
    private void buttonOutputVideo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Output Video File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP4", "*.mp4"),
                new FileChooser.ExtensionFilter("AVI", "*.avi"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File file = fileChooser.showSaveDialog(root.getScene().getWindow());

        if (file != null) {
            configuration.setOutputVideo(file);
        }
    }

    @FXML
    private void buttonHeadingFont(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Heading Font");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TTF", "*.ttf"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File file = fileChooser.showOpenDialog(root.getScene().getWindow());

        if (file != null && file.isFile()) {
            configuration.setHeadingFont(file);
        }
    }

    @FXML
    private void buttonHeadingLogo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Heading Logo");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File file = fileChooser.showOpenDialog(root.getScene().getWindow());

        if (file != null && file.isFile()) {
            configuration.setSeriesLogo(file);
        }
    }

    @FXML
    private void buttonBackdrop(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Background Image");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File file = fileChooser.showOpenDialog(root.getScene().getWindow());

        if (file != null && file.isFile()) {
            configuration.setBackdrop(file);
        }
    }

    @FXML
    private void buttonLogo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Background Logo");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File file = fileChooser.showOpenDialog(root.getScene().getWindow());

        if (file != null && file.isFile()) {
            configuration.setLogo(file);
        }
    }

    @FXML
    private void buttonFont (ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Font");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TTF", "*.ttf"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File file = fileChooser.showOpenDialog(root.getScene().getWindow());

        if (file != null && file.isFile()) {
            configuration.setFont(file);
        }
    }

    @FXML
    private void buttonAddPosition (ActionEvent event) {
        int next_index = configuration.getPointStructure().size();
        configuration.getPointStructure().add(new PointStructureItem(next_index, 0));
    }

    @FXML
    private void buttonDeletePosition (ActionEvent event) {
        configuration.getPointStructure().removeAll(tblPointStructure.getSelectionModel().getSelectedItems());

        Iterator<PointStructureItem> iterator = configuration.getPointStructure().iterator();
        Integer index = 0;
        List<PointStructureItem> newItems = new ArrayList<>();
        while (iterator.hasNext()) {
            PointStructureItem entry = iterator.next();
            if (!(entry.getFinishPosition() == index)) {
                entry.setFinishPosition(index);
                newItems.add(entry);
                iterator.remove();
            }
            index += 1;
        }
        configuration.getPointStructure().addAll(newItems);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JSONFile.set(null);
        txtFileName.textProperty().bind(Bindings.convert(JSONFile));

        configuration = new Configuration();
        addListeners();

        colFinishPosition.setCellValueFactory(
                new PropertyValueFactory<>("finishPosition")
        );
        colPoints.setCellValueFactory(
                new PropertyValueFactory<>("points")
        );
        colPoints.setCellFactory(param -> CustomCell.createIntegerEditCell());
        colPoints.setOnEditCommit(
                t -> (t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPoints(t.getNewValue())
        );

        colName.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        colDisplayName.setCellValueFactory(
                new PropertyValueFactory<>("displayName")
        );
        colDisplayName.setCellFactory(param -> CustomCell.createStringEditCell());
        colDisplayName.setOnEditCommit(
                t -> (t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setDisplayName(t.getNewValue())
        );
        colShortName.setCellValueFactory(
                new PropertyValueFactory<>("shortName")
        );
        colShortName.setCellFactory(param -> CustomCell.createStringEditCell());
        colShortName.setOnEditCommit(
                t -> (t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setShortName(t.getNewValue())
        );
        colCar.setCellValueFactory(
                param -> param.getValue().getCar() == null ?
                        new SimpleStringProperty(null) :
                        new SimpleStringProperty(param.getValue().getCar().getCarName())
        );
        colCar.setCellFactory(param -> CustomCell.createStringEditCell());
        colCar.setOnEditCommit(
                t -> {
                    Driver driver = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    if (driver.getCar() == null) {
                        driver.setCar(new Car(t.getNewValue(), new CarClass("", Color.rgb(255, 0, 0))));
                    } else {
                        driver.getCar().setCarName(t.getNewValue());
                    }
                }
        );
        colTeam.setCellValueFactory(
                new PropertyValueFactory<>("team")
        );
        colTeam.setCellFactory(param -> CustomCell.createStringEditCell());
        colTeam.setOnEditCommit(
                t -> (t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setTeam(t.getNewValue())
        );
        colSeriesPoints.setCellValueFactory(
                new PropertyValueFactory<>("seriesPoints")
        );
        colSeriesPoints.setCellFactory(param -> CustomCell.createIntegerEditCell());
        colSeriesPoints.setOnEditCommit(
                t -> (t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setSeriesPoints(t.getNewValue())
        );

        colCarName.setCellValueFactory(
                new PropertyValueFactory<>("carName")
        );
        colClassName.setCellValueFactory(
                param -> param.getValue() == null || param.getValue().getCarClass() == null ?
                        new SimpleStringProperty(null) :
                        new SimpleStringProperty(param.getValue().getCarClass().getClassName())
        );
        colClassName.setCellFactory(param -> CustomCell.createStringEditCell());
        colClassName.setOnEditCommit(
                event -> (event.getTableView().getItems().get(
                        event.getTablePosition().getRow())
                ).getCarClass().setClassName(event.getNewValue())
        );
        colClassColor.setCellValueFactory(
                param -> param.getValue() == null || param.getValue().getCarClass() == null ?
                        new SimpleObjectProperty<>(null) :
                        new SimpleObjectProperty<>(param.getValue().getCarClass().getClassColor())
        );
        colClassColor.setCellFactory(ColorTableCell::new);
        colClassColor.setOnEditCommit(
                event -> (event.getTableView().getItems().get(
                        event.getTablePosition().getRow())
                ).getCarClass().setClassColor(event.getNewValue())
        );
    }

    private void addListeners() {
        // Interface
        tabDrivers.setDisable(true);
        configuration.participantConfigurationProperty().sizeProperty().addListener((observable, oldValue, newValue) -> tabDrivers.setDisable(newValue.intValue() < 1));

        // Source Data
        txtSourceVideo.textProperty().bindBidirectional(configuration.sourceVideoProperty(), new StringConverter<File>() {
            @Override
            public String toString(File object) {
                if (object == null) {
                    return "";
                } else {
                    try {
                        return object.getCanonicalPath();
                    } catch (IOException e) {
                        return "";
                    }
                }
            }

            @Override
            public File fromString(String string) {
                return new File(string);
            }
        });
        txtSourceTelemetry.textProperty().bindBidirectional(configuration.sourceTelemetryProperty(), new StringConverter<File>() {
            @Override
            public String toString(File object) {
                if (object == null) {
                    return "";
                } else {
                    try {
                        return object.getCanonicalPath();
                    } catch (IOException e) {
                        return "";
                    }
                }
            }

            @Override
            public File fromString(String string) {
                return new File(string);
            }
        });

        // Source Parameters
        txtVideoStart.textProperty().bindBidirectional(configuration.videoStartTimeProperty(), new ConvertTime());
        txtVideoEnd.textProperty().bindBidirectional(configuration.videoEndTimeProperty(), new ConvertTime());
        txtVideoSync.textProperty().bindBidirectional(configuration.syncRacestartProperty(), new ConvertTime());

        // Output
        txtOutputVideo.textProperty().bindBidirectional(configuration.outputVideoProperty(), new StringConverter<File>() {
            @Override
            public String toString(File object) {
                if (object == null) {
                    return "";
                } else {
                    try {
                        return object.getCanonicalPath();
                    } catch (IOException e) {
                        return "";
                    }
                }
            }

            @Override
            public File fromString(String string) {
                return new File(string);
            }
        });

        // Headings
        txtHeadingText.textProperty().bindBidirectional(configuration.headingTextProperty());
        txtSubheadingText.textProperty().bindBidirectional(configuration.subheadingTextProperty());
        txtHeadingFont.textProperty().bindBidirectional(configuration.headingFontProperty(), new StringConverter<File>() {
            @Override
            public String toString(File object) {
                if (object == null) {
                    return "";
                } else {
                    try {
                        return object.getCanonicalPath();
                    } catch (IOException e) {
                        return "";
                    }
                }
            }

            @Override
            public File fromString(String string) {
                return new File(string);
            }
        });
        txtHeadingFontSize.textProperty().bindBidirectional(configuration.headingFontSizeProperty(), new NumberStringConverter());
        colorHeadingFontColor.valueProperty().bindBidirectional(configuration.headingFontColorProperty());
        colorHeadingColor.valueProperty().bindBidirectional(configuration.headingColorProperty());
        txtHeadingLogo.textProperty().bindBidirectional(configuration.seriesLogoProperty(), new StringConverter<File>() {
            @Override
            public String toString(File object) {
                if (object == null) {
                    return "";
                } else {
                    try {
                        return object.getCanonicalPath();
                    } catch (IOException e) {
                        return "";
                    }
                }
            }

            @Override
            public File fromString(String string) {
                return new File(string);
            }
        });

        // Backgrounds
        txtBackdrop.textProperty().bindBidirectional(configuration.backdropProperty(), new StringConverter<File>() {
            @Override
            public String toString(File object) {
                if (object == null) {
                    return "";
                } else {
                    try {
                        return object.getCanonicalPath();
                    } catch (IOException e) {
                        return "";
                    }
                }
            }

            @Override
            public File fromString(String string) {
                return new File(string);
            }
        });
        txtLogo.textProperty().bindBidirectional(configuration.logoProperty(), new StringConverter<File>() {
            @Override
            public String toString(File object) {
                if (object == null) {
                    return "";
                } else {
                    try {
                        return object.getCanonicalPath();
                    } catch (IOException e) {
                        return "";
                    }
                }
            }

            @Override
            public File fromString(String string) {
                return new File(string);
            }
        });
        txtLogoHeight.textProperty().bindBidirectional(configuration.logoHeightProperty(), new NumberStringConverter());
        txtLogoWidth.textProperty().bindBidirectional(configuration.logoWidthProperty(), new NumberStringConverter());

        // Font
        txtFont.textProperty().bindBidirectional(configuration.fontProperty(), new StringConverter<File>() {
            @Override
            public String toString(File object) {
                if (object == null) {
                    return "";
                } else {
                    try {
                        return object.getCanonicalPath();
                    } catch (IOException e) {
                        return "";
                    }
                }
            }

            @Override
            public File fromString(String string) {
                return new File(string);
            }
        });
        txtFontSize.textProperty().bindBidirectional(configuration.fontSizeProperty(), new NumberStringConverter());
        colorFontColor.valueProperty().bindBidirectional(configuration.fontColorProperty());

        // Layout
        txtMarginWidth.textProperty().bindBidirectional(configuration.marginProperty(), new NumberStringConverter());
        txtColumnMarginWidth.textProperty().bindBidirectional(configuration.columnMarginProperty(), new NumberStringConverter());
        txtResultLines.textProperty().bindBidirectional(configuration.resultLinesProperty(), new NumberStringConverter());

        // Options
        cbShowChampion.selectedProperty().bindBidirectional(configuration.showChampionProperty());
        txtBonusPoints.textProperty().bindBidirectional(configuration.pointStructureProperty(), new StringConverter<ObservableList<PointStructureItem>>() {
            @Override
            public String toString(ObservableList<PointStructureItem> object) {
                return Integer.toString(object.get(0).getPoints());
            }

            @Override
            public ObservableList<PointStructureItem> fromString(String string) {
                if (string.equals("")) return configuration.getPointStructure();
                ObservableList<PointStructureItem> list = configuration.getPointStructure();
                list.set(0, new PointStructureItem(0, Integer.valueOf(string)));
                return list;
            }
        });
        tblPointStructure.setItems(configuration.pointStructureProperty().filtered(pointStructureItem -> pointStructureItem.getFinishPosition() > 0));

        // Drivers (and teams, and cars, oh my!)
        tblDrivers.setItems(configuration.participantConfigurationProperty());
        tblCars.setItems(configuration.carsProperty());
    }

    private void populateDrivers() {
        File[] files = new File(txtSourceTelemetry.getText()).listFiles((dir, name) -> name.matches(".*pdata.*"));

        if (files == null) return;

        Arrays.sort(files, (file1, file2) -> {
            Integer n1 = Integer.valueOf(file1.getName().replaceAll("[^\\d]", ""));
            Integer n2 = Integer.valueOf(file2.getName().replaceAll("[^\\d]", ""));
            return Integer.compare(n1, n2);
        });

        List<List<String>> allNames = new ArrayList<>();
        List<String> names = new ArrayList<>();
        Integer numParticipants = null;

        for (File file : files) {
            if (file.length() == 1367) {
                try {
                    TelemetryDataPacket packet = new TelemetryDataPacket(
                            ByteBuffer.wrap(Files.readAllBytes(file.toPath()))
                    );
                    if (packet.getRaceState() == 2) {
                        if (numParticipants == null || numParticipants != packet.getNumParticipants()) {
                            System.out.println(packet.getNumParticipants());
                            numParticipants = packet.getNumParticipants();
                            names = new ArrayList<>();
                        }
                    } else {
                        numParticipants = null;
                        names = new ArrayList<>();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (file.length() == 1347) {
                try {
                    ParticipantPacket packet = new ParticipantPacket(
                            ByteBuffer.wrap(Files.readAllBytes(file.toPath()))
                    );

                    if (numParticipants != null && names.size() < numParticipants) {
                        names.addAll(packet.getNames().stream()
                                .limit(numParticipants)
                                .map(simpleStringProperty -> simpleStringProperty.get().trim())
                                .collect(Collectors.toList()));

                        System.out.println(String.format("Participants: %1$d", names.size()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (file.length() == 1028) {
                try {
                    AdditionalParticipantPacket packet = new AdditionalParticipantPacket(
                            ByteBuffer.wrap(Files.readAllBytes(file.toPath()))
                    );

                    if (numParticipants != null && names.size() < numParticipants) {
                        names.addAll(packet.getNames().stream()
                                .limit(numParticipants)
                                .map(simpleStringProperty -> simpleStringProperty.get().trim())
                                .collect(Collectors.toList()));

                        System.out.println(String.format("Participants: %1$d", names.size()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (numParticipants != null && names.size() >= numParticipants) {
                if (allNames.size() == 0 || !allNames.get(allNames.size() - 1).equals(names)) {
                    allNames.add(names);
                }
            }
        }

        Set<String> masterNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        masterNames.addAll(allNames.get(0));

        List<String> oldNames = allNames.get(0);
        for (List<String> newNames : allNames.subList(1, allNames.size())) {
            ListIterator<String> iterator = newNames.listIterator();
            while (iterator.hasNext()) {
                int nextIndex = iterator.nextIndex();
                String newName = iterator.next();

                if (!oldNames.get(nextIndex).equals(newName)) {
                    String oldName = oldNames.get(oldNames.size() - 1);
                    String name = oldName;
                    int minLength = Math.min(oldName.length(), newName.length());
                    for (int i = 0; i < minLength; i++) {
                        if (oldName.charAt(i) != newName.charAt(i)) {
                            name = oldName.substring(0, i);
                            break;
                        }
                    }
                    masterNames.remove(oldName);
                    masterNames.add(name);
                }
            }
            oldNames = newNames;
        }

        ObservableList<Driver> drivers = FXCollections.observableArrayList(param -> new Observable[]{param.getCar().carNameProperty()});
        drivers.addAll(masterNames
                .stream()
                .map(Driver::new)
                .collect(Collectors.toList()));

        configuration.setParticipantConfiguration(drivers);
    }

    private static class ConvertTime extends StringConverter<Number> {
        @Override
        public String toString(Number object) {
            String returnValue = "";
            Double inputValue = object.doubleValue();

            String fractionalPart = object.toString().substring(object.toString().indexOf('.') + 1);
            if (fractionalPart.equals("0")) {
                returnValue = ".00";
            } else {
                returnValue = "." + fractionalPart;
            }

            if ((int) (inputValue / 3600) > 0) {
                returnValue = String.format("%d", (int) (inputValue / 3600)) + ":" + String.format("%02d", (int) ((inputValue % 3600) / 60)) + ":" + String.format("%02d", (int) (inputValue % 60)) + returnValue;
            } else {
                if ((int) ((inputValue % 3600) / 60) > 0) {
                    returnValue = String.format("%d", (int) ((inputValue % 3600) / 60)) + ":" + String.format("%02d", (int) (inputValue % 60)) + returnValue;
                } else {
                    returnValue = "0:" + String.format("%02d", (int) (inputValue % 60)) + returnValue;
                }
            }

            return returnValue;
        }

        @Override
        public Number fromString(String string) {
            Pattern regex = Pattern.compile("(?:^(\\d*):([0-5]?\\d):([0-5]?\\d(?:\\.\\d*)?)$|^(\\d*):([0-5]?\\d(?:\\.\\d*)?)$|^(\\d*(?:\\.\\d*)?)$)");
            Matcher matches = regex.matcher(string);

            if (!matches.matches() || string.equals("")) {
                return 0;
            }

            Double hours = 0d;
            Double minutes = 0d;
            Double seconds = 0d;

            if (matches.group(1) != null) {
                hours = Double.valueOf(matches.group(1)) * 60 * 60;
                minutes = Double.valueOf(matches.group(2)) * 60;
                seconds = Double.valueOf(matches.group(3));
            } else if (matches.group(4) != null) {
                minutes = Double.valueOf(matches.group(4)) * 60;
                seconds = Double.valueOf(matches.group(5));
            } else if (matches.group(6) != null) {
                seconds = Double.valueOf(matches.group(6));
            }

            return hours + minutes + seconds;
        }
    }

    /*
     * From http://info.michael-simons.eu/2014/10/27/custom-editor-components-in-javafx-tablecells/
     */
    private class ColorTableCell<T> extends TableCell<T, Color> {
        private final ColorPicker colorPicker;

        public ColorTableCell(TableColumn<T, Color> column) {
            this.colorPicker = new ColorPicker();
            this.colorPicker.editableProperty().bind(column.editableProperty());
            this.colorPicker.disableProperty().bind(column.editableProperty().not());
            this.colorPicker.setOnShowing(event -> {
                final TableView<T> tableView = getTableView();
                tableView.getSelectionModel().select(getTableRow().getIndex());
                tableView.edit(tableView.getSelectionModel().getSelectedIndex(), column);
            });
            this.colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (isEditing()) {
                    commitEdit(newValue);
                }
            });
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }

        @Override
        protected void updateItem(Color item, boolean empty) {
            super.updateItem(item, empty);

            setText(null);
            if (empty) {
                setGraphic(null);
            } else {
                this.colorPicker.setValue(item);
                this.setGraphic(this.colorPicker);
            }
        }
    }
}