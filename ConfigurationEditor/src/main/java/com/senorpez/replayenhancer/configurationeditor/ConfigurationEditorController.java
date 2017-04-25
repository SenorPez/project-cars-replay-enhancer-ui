package com.senorpez.replayenhancer.configurationeditor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.humble.video.Demuxer;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
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
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigurationEditorController implements Initializable {
    private final SimpleObjectProperty<File> JSONFile = new SimpleObjectProperty<>();
    private Configuration configuration;

    @FXML
    private VBox root;
    
    @FXML
    private Tab tabDrivers;
    
    @FXML
    private TextField txtSourceVideo;

    @FXML
    private Label txtFPSLabel;

    @FXML
    private CheckBox cbFPS;

    @FXML
    private TextField txtFPS;

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
    private TextArea txtPythonOutput;
    
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
    private TextField txtLeaderStandingsLines;

    @FXML
    private TextField txtWindowStandingsLines;
    
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
    private CheckBox cbShowTimer;

    @FXML
    private CheckBox cbShowChampion;

    @FXML
    private Label lblChampionHeight;

    @FXML
    private TextField txtChampionHeight;

    @FXML
    private Label lblChampionWidth;

    @FXML
    private TextField txtChampionWidth;

    @FXML
    private Label lblChampionColor;

    @FXML
    private ColorPicker colorChampionColor;

    @FXML
    private CheckBox cbHideSeriesZeros;

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
    private TableColumn<Driver, String> colPointsAdjust;

    @FXML
    private TableView<Driver> tblAddDrivers;

    @FXML
    private TableColumn<Driver, String> colAddName;

    @FXML
    private TableColumn<Driver, String> colAddCar;

    @FXML
    private TableColumn<Driver, String> colAddTeam;

    @FXML
    private TableColumn<Driver, Integer> colAddSeriesPoints;

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

    @FXML
    private GridPane gridProgress;

    @FXML
    private ProgressBar prgProgress;

    @FXML
    private GridPane gridPython;

    @FXML
    private Button btnMakeSyncVideo;

    @FXML
    private Button btnMakeVideo;

    private DriverPopulator driverPopulator = null;
    private PythonExecutor pythonExecutor = null;

    private final SimpleDoubleProperty sourceVideoDuraton = new SimpleDoubleProperty();
    private ConfigurationValidator configurationValidator = new ConfigurationValidator();

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
        configurationValidator.setInvalidVideoSyncTime(false);

        Pattern regex = Pattern.compile("(?:^-?(\\d*):([0-5]?\\d):([0-5]?\\d(?:\\.\\d*)?)$|^-?(\\d*):([0-5]?\\d(?:\\.\\d*)?)$|^-?(\\d*(?:\\.\\d*)?)$)");
        Matcher match = regex.matcher(txtSource.getText());
        if (!match.matches()) {
            txtSource.setStyle("-fx-text-inner-color: red");
            configurationValidator.setInvalidVideoSyncTime(true);
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
    private void buttonSourceTelemetry(ActionEvent event) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open Source Telemetry Directory");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = directoryChooser.showDialog(root.getScene().getWindow());

        configurationValidator.setInvalidSourceTelemetry(true);
        if (file != null && file.isDirectory()) {
            driverPopulator = new DriverPopulator(file.toPath());
            ChangeListener<Number> progressListener = (observable, oldValue, newValue) -> prgProgress.setProgress(Math.max(0, newValue.doubleValue()));

            driverPopulator.setOnScheduled(serviceEvent -> prgProgress.setProgress(0));
            driverPopulator.setOnRunning(serviceEvent -> {
                driverPopulator.progressProperty().addListener(progressListener);
                gridProgress.setVisible(true);
            });
            driverPopulator.setOnSucceeded(serviceEvent -> {
                configuration.setSourceTelemetry(file);
                configuration.getParticipantConfiguration().setAll(driverPopulator.getValue());
                configurationValidator.setInvalidSourceTelemetry(false);
                driverPopulator.progressProperty().removeListener(progressListener);
                gridProgress.setVisible(false);
            });
            driverPopulator.setOnCancelled(serviceEvent -> {
                driverPopulator.progressProperty().removeListener(progressListener);
                gridProgress.setVisible(false);
            });
            driverPopulator.setOnFailed(serviceEvent -> {
                driverPopulator.progressProperty().removeListener(progressListener);
                gridProgress.setVisible(false);
            });

            driverPopulator.start();
        }
    }

    @FXML
    private void buttonCancelDriver(ActionEvent event) {
        driverPopulator.cancel();
    }

    private static class DriverPopulator extends Service<List<Driver>> {
        private final Path telemetryDirectory;
        private final Set<String> names = new HashSet<>();
        private Integer raceState = null;
        private Boolean raceFinished = false;
        private Boolean clearDrivers = false;

        public DriverPopulator(Path telemetryDirectory) {
            this.telemetryDirectory = telemetryDirectory;
        }

        private void dispatchPacket(Path packetPath) {
            Long length = packetPath.toFile().length();
            try {
                ByteBuffer packetData = ByteBuffer.wrap(Files.readAllBytes(packetPath));
                if (length == 1367) {
                    processPacket(new TelemetryDataPacket(packetData));
                } else if (length == 1347) {
                    if (raceState == 3) { raceFinished = true; }

                    if (raceState == 2 && raceFinished) {
                        clearDrivers = true;
                    }

                    if (clearDrivers) {
                        clearDrivers = false;
                        raceFinished = false;
                        names.clear();
                    }
                    processPacket(new ParticipantPacket(packetData));
                } else if (length == 1028) {
                    if (raceState == 3) { raceFinished = true; }

                    if (raceState == 2 && raceFinished) {
                        clearDrivers = true;
                    }

                    if (clearDrivers) {
                        clearDrivers = false;
                        raceFinished = false;
                        names.clear();
                    }
                    processPacket(new ParticipantPacket(packetData));
                }
            } catch (IOException e) {
                if (!getState().equals(State.CANCELLED)) {
                    e.printStackTrace();
                }
            }
        }

        private void processPacket(AdditionalParticipantPacket packet) {
            names.addAll(
                    packet.getNames().stream()
                            .map(SimpleStringProperty::get)
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.toCollection(ArrayList::new))
            );
        }

        private void processPacket(ParticipantPacket packet) {
            names.addAll(
                    packet.getNames().stream()
                            .map(SimpleStringProperty::get)
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.toCollection(ArrayList::new))
            );
        }

        private void processPacket(TelemetryDataPacket packet) {
            raceState = packet.getRaceState();
        }

        @Override
        protected Task<List<Driver>> createTask() {
            return new Task<List<Driver>>() {
                @Override
                protected List<Driver> call() throws Exception {

                    try (Stream<Path> files = Files.list(telemetryDirectory)) {
                        List<Path> telemetry = files.collect(Collectors.toCollection(ArrayList::new));
                        telemetry.removeIf(path -> path.toFile().getName().replaceAll("[^\\d]", "").equals(""));
                        telemetry.sort((o1, o2) -> {
                            Integer n1 = Integer.valueOf(o1.toFile().getName().replaceAll("[^\\d]", ""));
                            Integer n2 = Integer.valueOf(o2.toFile().getName().replaceAll("[^\\d]", ""));
                            return n1.compareTo(n2);
                        });

                        telemetry.stream()
                                .filter(path -> !isCancelled())
                                .forEach(path -> {
                                    updateProgress(Long.valueOf(path.toFile().getName().replaceAll("[^\\d]", "")), telemetry.size());
                                    dispatchPacket(path);
                                });
                    }
                    return names.stream().map(Driver::new).collect(Collectors.toCollection(ArrayList::new));
                }
            };
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
        configurationValidator.setInvalidOutputVideo(true);

        if (file != null) {
            configuration.setOutputVideo(file);
            configurationValidator.setInvalidOutputVideo(false);
        }
    }

    @FXML
    private void buttonMakeSyncVideo(ActionEvent event) throws IOException{
        createVideo(true);
    }

    @FXML
    private void buttonMakeVideo(ActionEvent event) throws IOException{
        createVideo(false);
    }

    private void createVideo(Boolean syncVideo) throws IOException {
        menuFileSave();

        Integer fps;
        try {
            fps = Integer.valueOf(txtFPS.getText());
        } catch (NumberFormatException e) {
            fps = null;
        }

        ArrayList<String> command = new ArrayList<>();
        command.add("replayenhancer");
        if (fps != null) {
            command.add("-f");
            command.add(fps.toString());
        }
        if (syncVideo) {
            command.add("-s");
        }
        command.add(txtFileName.getText());

        pythonExecutor = new PythonExecutor(command.toArray(new String[command.size()]));
        ChangeListener<String> messageListener = (observable, oldValue, newValue) -> {
            txtPythonOutput.clear();
            txtPythonOutput.appendText(newValue);
        };

        pythonExecutor.setOnScheduled(serviceEvent -> txtPythonOutput.clear());
        pythonExecutor.setOnRunning(serviceEvent -> {
            pythonExecutor.messageProperty().addListener(messageListener);
            gridPython.setVisible(true);
            btnMakeSyncVideo.disableProperty().unbind();
            btnMakeVideo.disableProperty().unbind();
            btnMakeSyncVideo.setDisable(true);
            btnMakeVideo.setDisable(true);
        });
        pythonExecutor.setOnSucceeded(serviceEvent -> {
            pythonExecutor.messageProperty().removeListener(messageListener);
            gridPython.setVisible(false);
            btnMakeSyncVideo.setDisable(false);
            btnMakeVideo.setDisable(false);
            btnMakeSyncVideo.disableProperty().bind(configurationValidator.invalidConfigurationProperty());
            btnMakeVideo.disableProperty().bind(configurationValidator.invalidConfigurationProperty());
        });
        pythonExecutor.setOnCancelled(serviceEvent -> {
            pythonExecutor.messageProperty().removeListener(messageListener);
            txtPythonOutput.clear();
            txtPythonOutput.appendText("Cancelled.");
            gridPython.setVisible(false);
            btnMakeSyncVideo.setDisable(false);
            btnMakeVideo.setDisable(false);
            btnMakeSyncVideo.disableProperty().bind(configurationValidator.invalidConfigurationProperty());
            btnMakeVideo.disableProperty().bind(configurationValidator.invalidConfigurationProperty());
        });
        pythonExecutor.setOnFailed(serviceEvent -> {
            pythonExecutor.messageProperty().removeListener(messageListener);
            txtPythonOutput.clear();
            txtPythonOutput.appendText("Error.");
            gridPython.setVisible(false);
            btnMakeSyncVideo.setDisable(false);
            btnMakeVideo.setDisable(false);
            btnMakeSyncVideo.disableProperty().bind(configurationValidator.invalidConfigurationProperty());
            btnMakeVideo.disableProperty().bind(configurationValidator.invalidConfigurationProperty());
        });

        pythonExecutor.start();
    }

    @FXML
    private void buttonCancelPython(ActionEvent event) {
        pythonExecutor.cancel();
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
    private void buttonAddAdditionalDriver (ActionEvent event) {
        configuration.getAdditionalParticipantConfiguration().add(
                new Driver("Additional Driver")
        );
    }

    @FXML
    private void buttonDeleteAdditionalDriver (ActionEvent event) {
        configuration.getAdditionalParticipantConfiguration().removeAll(tblAddDrivers.getSelectionModel().getSelectedItems());
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

        gridProgress.managedProperty().bind(gridProgress.visibleProperty());
        gridProgress.setVisible(false);

        gridPython.managedProperty().bind(gridPython.visibleProperty());
        gridPython.setVisible(false);

        btnMakeSyncVideo.disableProperty().bind(configurationValidator.invalidConfigurationProperty());
        btnMakeVideo.disableProperty().bind(configurationValidator.invalidConfigurationProperty());

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

        colPointsAdjust.setCellValueFactory(
                new PropertyValueFactory<>("pointsAdjust")
        );
        colPointsAdjust.setCellFactory(param -> CustomCell.createStringEditCell());
        colPointsAdjust.setOnEditCommit(
                t -> (t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPointsAdjust(t.getNewValue())
        );

        colAddName.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        colAddName.setCellFactory(param -> CustomCell.createStringEditCell());
        colAddName.setOnEditCommit(
                t -> {
                    Driver item = (t.getTableView().getItems().get(t.getTablePosition().getRow()));
                    item.setName(t.getNewValue());
                    item.setDisplayName(t.getNewValue());
                    item.setShortName(t.getNewValue());
                }
        );

        colAddCar.setCellValueFactory(
                param -> param.getValue().getCar() == null ?
                        new SimpleStringProperty(null) :
                        new SimpleStringProperty(param.getValue().getCar().getCarName())
        );
        colAddCar.setCellFactory(param -> CustomCell.createStringEditCell());
        colAddCar.setOnEditCommit(
                t -> {
                    Driver driver = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    if (driver.getCar() == null) {
                        driver.setCar(new Car(t.getNewValue(), new CarClass("", Color.rgb(255, 0, 0))));
                    } else {
                        driver.getCar().setCarName(t.getNewValue());
                    }
                }
        );

        colAddTeam.setCellValueFactory(
                new PropertyValueFactory<>("team")
        );
        colAddTeam.setCellFactory(param -> CustomCell.createStringEditCell());
        colAddTeam.setOnEditCommit(
                t -> (t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setTeam(t.getNewValue())
        );

        colAddSeriesPoints.setCellValueFactory(
                new PropertyValueFactory<>("seriesPoints")
        );
        colAddSeriesPoints.setCellFactory(param -> CustomCell.createIntegerEditCell());
        colAddSeriesPoints.setOnEditCommit(
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
        colClassName.setOnEditCommit(event -> {
            Map<String, CarClass> carClassMap = event.getTableView().getItems().stream().map(Car::getCarClass).collect(Collectors.toMap(CarClass::getClassName, carClass -> carClass, (carClass, carClass2) -> carClass));
            if (carClassMap.containsKey(event.getNewValue())) {
                (event.getTableView().getItems().get(event.getTablePosition().getRow())).setCarClass(carClassMap.get(event.getNewValue()));
            } else {
                (event.getTableView().getItems().get(event.getTablePosition().getRow())).setCarClass(new CarClass(event.getNewValue(), Color.RED));
            }
            event.getTableView().refresh();
        });
        colClassColor.setCellValueFactory(
                param -> param.getValue() == null || param.getValue().getCarClass() == null ?
                        new SimpleObjectProperty<>(null) :
                        new SimpleObjectProperty<>(param.getValue().getCarClass().getClassColor())
        );
        colClassColor.setCellFactory(ColorTableCell::new);
        colClassColor.setOnEditCommit(event -> {
            (event.getTableView().getItems().get(event.getTablePosition().getRow())).getCarClass().setClassColor(event.getNewValue());
            event.getTableView().refresh();
        });
    }

    private static Double getVideoDuration(String video) {
        final Demuxer demuxer = Demuxer.make();
        try {
            demuxer.open(video, null, false, true, null, null);
        } catch (InterruptedException | IOException e) {
            return null;
        }
        return demuxer.getDuration() / 1000000.0;
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

        if (txtSourceVideo.getText() == null || txtSourceVideo.getText().isEmpty()) {
            txtFPSLabel.setText("Use Default FPS (30):");
        } else {
            txtFPSLabel.setText("Use Source Video FPS:");
        }

        txtSourceVideo.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                txtFPSLabel.setText("Use Default FPS (30):");
                sourceVideoDuraton.setValue(null);
            } else {
                txtFPSLabel.setText("Use Source Video FPS:");
                sourceVideoDuraton.setValue(getVideoDuration(newValue));
            }
        });

        cbFPS.selectedProperty().addListener((observable, oldValue, newValue) -> {
            txtFPS.setEditable(!newValue);
            txtFPS.setDisable(newValue);

            if (newValue) {
                txtFPS.setText(null);
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
        configuration.videoStartTimeProperty().addListener((observable, oldValue, newValue) -> {
            txtVideoStart.setStyle("-fx-text-inner-color: black");
            configurationValidator.setInvalidVideoStartTime(false);

            if (newValue != null) {
                final Pattern regex = Pattern.compile("(?:^(\\d*):([0-5]?\\d):([0-5]?\\d(?:\\.\\d*)?)$|^(\\d*):([0-5]?\\d(?:\\.\\d*)?)$|^(\\d*(?:\\.\\d*)?)$)");
                final Matcher match = regex.matcher(txtVideoStart.getText());

                if (!match.matches()
                        || newValue.doubleValue() > sourceVideoDuraton.get()
                        || newValue.doubleValue() > configuration.videoEndTimeProperty().get()) {
                    txtVideoStart.setStyle("-fx-text-inner-color: red");
                    configurationValidator.setInvalidVideoStartTime(true);
                }
            }
        });

        txtVideoEnd.textProperty().bindBidirectional(configuration.videoEndTimeProperty(), new ConvertTime());
        configuration.videoEndTimeProperty().addListener((observable, oldValue, newValue) -> {
            txtVideoEnd.setStyle("-fx-text-inner-color: black");
            configurationValidator.setInvalidVideoEndTime(false);

            if (newValue != null) {
                final Pattern regex = Pattern.compile("(?:^(\\d*):([0-5]?\\d):([0-5]?\\d(?:\\.\\d*)?)$|^(\\d*):([0-5]?\\d(?:\\.\\d*)?)$|^(\\d*(?:\\.\\d*)?)$)");
                final Matcher match = regex.matcher(txtVideoEnd.getText());

                if (!match.matches()
                        || newValue.doubleValue() > sourceVideoDuraton.get()
                        || newValue.doubleValue() < configuration.videoStartTimeProperty().get()) {
                    txtVideoEnd.setStyle("-fx-text-inner-color: red");
                    configurationValidator.setInvalidVideoEndTime(true);
                }
            }
        });

        sourceVideoDuraton.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (configuration.videoEndTimeProperty().get() == 0) {
                    configuration.videoEndTimeProperty().setValue(newValue);
                }

                if (configuration.videoStartTimeProperty().get() > sourceVideoDuraton.get()) {
                    txtVideoStart.setStyle("-fx-text-inner-color: red");
                    configurationValidator.setInvalidVideoStartTime(true);
                }

                if (configuration.videoEndTimeProperty().get() > sourceVideoDuraton.get()) {
                    txtVideoEnd.setStyle("-fx-text-inner-color: red");
                    configurationValidator.setInvalidVideoEndTime(true);
                }
            }
        });

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
        txtLeaderStandingsLines.textProperty().bindBidirectional(configuration.leaderStandingsLinesProperty(), new NumberStringConverter());
        txtWindowStandingsLines.textProperty().bindBidirectional(configuration.windowStandingsLinesProperty(), new NumberStringConverter());
        cbShowTimer.selectedProperty().bindBidirectional(configuration.showTimerProperty());

        // Options
        cbShowChampion.selectedProperty().bindBidirectional(configuration.showChampionProperty());

        txtChampionWidth.textProperty().bindBidirectional(configuration.championWidthProperty(), new NumberStringConverter());
        lblChampionWidth.managedProperty().bind(cbShowChampion.selectedProperty());
        lblChampionWidth.visibleProperty().bind(cbShowChampion.selectedProperty());
        txtChampionWidth.managedProperty().bind(cbShowChampion.selectedProperty());
        txtChampionWidth.visibleProperty().bind(cbShowChampion.selectedProperty());

        txtChampionHeight.textProperty().bindBidirectional(configuration.championHeightProperty(), new NumberStringConverter());
        lblChampionHeight.managedProperty().bind(cbShowChampion.selectedProperty());
        lblChampionHeight.visibleProperty().bind(cbShowChampion.selectedProperty());
        txtChampionHeight.managedProperty().bind(cbShowChampion.selectedProperty());
        txtChampionHeight.visibleProperty().bind(cbShowChampion.selectedProperty());

        colorChampionColor.valueProperty().bindBidirectional(configuration.championColorProperty());
        lblChampionColor.managedProperty().bind(cbShowChampion.selectedProperty());
        lblChampionColor.visibleProperty().bind(cbShowChampion.selectedProperty());
        colorChampionColor.managedProperty().bind(cbShowChampion.selectedProperty());
        colorChampionColor.visibleProperty().bind(cbShowChampion.selectedProperty());

        cbHideSeriesZeros.selectedProperty().bindBidirectional(configuration.hideSeriesZerosProperty());

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
        SortedList<Driver> sortedDrivers = configuration.participantConfigurationProperty().sorted((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        sortedDrivers.comparatorProperty().bind(tblDrivers.comparatorProperty());
        tblDrivers.setItems(sortedDrivers);
        tblDrivers.getColumns().get(0).setSortType(TableColumn.SortType.ASCENDING);
        tblDrivers.getSortOrder().clear();
        tblDrivers.getSortOrder().add(tblDrivers.getColumns().get(0));
        tblDrivers.sort();

        SortedList<Driver> sortedAddDrivers = configuration.additionalParticipantConfigurationProperty().sorted((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        sortedAddDrivers.comparatorProperty().bind(tblAddDrivers.comparatorProperty());
        tblAddDrivers.setItems(sortedAddDrivers);
        tblAddDrivers.getColumns().get(0).setSortType(TableColumn.SortType.ASCENDING);
        tblAddDrivers.getSortOrder().clear();
        tblAddDrivers.getSortOrder().add(tblAddDrivers.getColumns().get(0));
        tblAddDrivers.sort();

        SortedList<Car> sortedCars = configuration.carsProperty().sorted((o1, o2) -> o1.getCarName().compareToIgnoreCase(o2.getCarName()));
        sortedCars.comparatorProperty().bind(tblCars.comparatorProperty());
        tblCars.setItems(sortedCars);
        tblCars.getColumns().get(0).setSortType(TableColumn.SortType.ASCENDING);
        tblCars.getSortOrder().clear();
        tblCars.getSortOrder().add(tblCars.getColumns().get(0));
        tblCars.sort();
    }

    private static class ConvertTime extends StringConverter<Number> {
        @Override
        public String toString(Number object) {
            String returnValue;
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

    private static class PythonExecutor extends Service<Integer> {
        private final String[] command;
        private Process process;

        public PythonExecutor(String[] command) {
            this.command = command;
        }

        @Override
        protected void cancelled() {
            super.cancelled();
            try {
                process.getInputStream().close();
                process.getErrorStream().close();
                process.getOutputStream().close();
                process.destroy();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Task<Integer> createTask() {
            return new Task<Integer>() {
                @Override
                protected Integer call() throws Exception {
                    updateMessage("");
                    String outputText = "";
                    String commandString = "";
                    for (String string : command) {
                        commandString += " " + string;
                    }
                    outputText += commandString + "\n\n";
                    updateMessage(outputText);
                    System.out.println("Running Command: " + commandString);
                    process = new ProcessBuilder(command).redirectErrorStream(true).start();

                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String s;
                        while ((s = br.readLine()) != null) {
                            outputText += s + "\n";
                            updateMessage(outputText);
                            System.out.println(s);
                        }
                        return process.waitFor();
                    } catch (IOException e) {
                        if (!isCancelled()) {
                            e.printStackTrace();
                            return -1;
                        } else return 0;
                    }
                }
            };
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
            this.colorPicker.setOnHiding(event -> {
                if (isEditing()) {
                    commitEdit(this.colorPicker.getValue());
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

    private class ConfigurationValidator {
        private final SimpleBooleanProperty invalidConfiguration = new SimpleBooleanProperty(true);

        private final SimpleBooleanProperty invalidSourceTelemetry = new SimpleBooleanProperty(true);
        private final SimpleBooleanProperty invalidOutputVideo = new SimpleBooleanProperty(true);
        private final SimpleBooleanProperty invalidVideoStartTime = new SimpleBooleanProperty(false);
        private final SimpleBooleanProperty invalidVideoEndTime = new SimpleBooleanProperty(false);
        private final SimpleBooleanProperty invalidVideoSyncTime = new SimpleBooleanProperty(false);

        public ConfigurationValidator() {
            ChangeListener<Boolean> listener = (observable, oldValue, newValue) -> invalidConfiguration.setValue(checkAll());

            invalidSourceTelemetry.addListener(listener);
            invalidOutputVideo.addListener(listener);
            invalidVideoStartTime.addListener(listener);
            invalidVideoEndTime.addListener(listener);
            invalidVideoSyncTime.addListener(listener);
        }

        private Boolean checkAll() {
            return (invalidSourceTelemetry.getValue()
                || invalidOutputVideo.getValue()
                || invalidVideoStartTime.getValue()
                || invalidVideoEndTime.getValue()
                || invalidVideoSyncTime.getValue());
        }

        public boolean getInvalidConfiguration() {
            return invalidConfiguration.get();
        }

        public SimpleBooleanProperty invalidConfigurationProperty() {
            return invalidConfiguration;
        }

        public void setInvalidSourceTelemetry(boolean invalidSourceTelemetry) {
            this.invalidSourceTelemetry.set(invalidSourceTelemetry);
        }

        public void setInvalidOutputVideo(boolean invalidOutputVideo) {
            this.invalidOutputVideo.set(invalidOutputVideo);
        }

        public void setInvalidVideoStartTime(boolean invalidVideoStartTime) {
            this.invalidVideoStartTime.set(invalidVideoStartTime);
        }

        public void setInvalidVideoEndTime(boolean invalidVideoEndTime) {
            this.invalidVideoEndTime.set(invalidVideoEndTime);
        }

        public void setInvalidVideoSyncTime(boolean invalidVideoSyncTime) {
            this.invalidVideoSyncTime.set(invalidVideoSyncTime);
        }
    }
}