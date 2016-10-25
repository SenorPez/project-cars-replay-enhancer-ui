/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurationeditor;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 *
 * @author SenorPez
 */
public class ReplayEnhancerUIController implements Initializable {
    private SimpleObjectProperty<File> JSONFile;
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

    @FXML
    private void menuFileNew() {
        configuration = new Configuration();
        addListeners();
        JSONFile = new SimpleObjectProperty<>();
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
            updateConfiguration(file, configuration);

            JSONFile.set(file);
        }
    }

    private static void updateConfiguration(File file, Configuration configuration) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String data = Files.lines(file.toPath()).collect(Collectors.joining());
        mapper.readerForUpdating(configuration).readValue(data);
    }
    
    @FXML
    private void menuFileSave() throws IOException {
        if (JSONFile == null) {
            menuFileSaveAs();
        }
        writeJSONFile(JSONFile.get(), configuration);
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

    private static File chooseJSONFile(Pane root) throws IOException {
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

    private static class ConvertTime extends StringConverter<Number> {
        @Override
        public String toString(Number object) {
            String returnValue = "";
            Double inputValue = object.doubleValue();

            if ((int) (inputValue % 1) > 0) {
                returnValue = "." + String.format(".%d", (int) (inputValue % 1));
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

            if (!matches.matches()) {
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
            if (!entry.getFinishPosition().equals(index)) {
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
        JSONFile = new SimpleObjectProperty<>();
        txtFileName.textProperty().bind(Bindings.convert(JSONFile));

        configuration = new Configuration();
        addListeners();

        colFinishPosition.setCellValueFactory(
            new PropertyValueFactory<>("finishPosition")
        );
        colPoints.setCellValueFactory(
            new PropertyValueFactory<>("points")
        );
        colPoints.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
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
        colDisplayName.setCellFactory(TextFieldTableCell.forTableColumn());
        colDisplayName.setOnEditCommit(
                t -> (t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                        ).setName(t.getNewValue())
        );
        colShortName.setCellValueFactory(
            new PropertyValueFactory<>("shortName")
        );
        colShortName.setCellFactory(TextFieldTableCell.forTableColumn());
        colShortName.setOnEditCommit(
                t -> (t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                        ).setShortName(t.getNewValue())
        );
        colCar.setCellValueFactory(
                param -> {
                    Car car = param.getValue().getCar();
                    if (car == null) {
                        return new SimpleStringProperty("");
                    } else {
                        return new SimpleStringProperty(car.getCarName());
                    }
                }
        );
        colCar.setCellFactory(TextFieldTableCell.forTableColumn());
        colCar.setOnEditCommit(
                t -> (t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                        ).setCar(new Car(t.getNewValue()))
        );
        colTeam.setCellValueFactory(
            new PropertyValueFactory<>("team")
        );
        colTeam.setCellFactory(TextFieldTableCell.forTableColumn());
        colTeam.setOnEditCommit(
                t -> (t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                        ).setTeam(t.getNewValue())
        );
        colSeriesPoints.setCellValueFactory(
            new PropertyValueFactory<>("seriesPoints")
        );
        colSeriesPoints.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colSeriesPoints.setOnEditCommit(
                t -> (t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                        ).setSeriesPoints(t.getNewValue())
        );

//        colCarName.setCellValueFactory(
//                new PropertyValueFactory<Car, String>("carName")
//        );
//        colCarName.setCellFactory(TextFieldTableCell.forTableColumn());
//        colCarName.setOnEditCommit(
//                new EventHandler<CellEditEvent<Car, String>>() {
//                    @Override
//                    public void handle(CellEditEvent<Car, String> event) {
//                        ((Car) event.getTableView().getItems().get(
//                                event.getTablePosition().getRow())
//                        ).setCarName(event.getNewValue());
//                    }
//                }
//        );
//
//        colClassName.setCellValueFactory(
//                new Callback<TableColumn.CellDataFeatures<Car, String>, ObservableValue<String>>() {
//                    @Override
//                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Car, String> param) {
//                        if (param.getValue().getCarClass() == null) {
//                            return new SimpleStringProperty("");
//                        }
//                        return new SimpleStringProperty(param.getValue().getCarClass().getClassName());
//                    }
//                }
//        );
//        colClassName.setCellFactory(TextFieldTableCell.forTableColumn());
//        colClassName.setOnEditCommit(
//                new EventHandler<CellEditEvent<Car, String>>() {
//                    @Override
//                    public void handle(CellEditEvent<Car, String> event) {
//                        ((Car) event.getTableView().getItems().get(
//                                event.getTablePosition().getRow())
//                        ).setCarName(event.getNewValue());
//                    }
//                }
//        );
//        colClassColor.setCellValueFactory(
//                new Callback<TableColumn.CellDataFeatures<Car, Color>, ObservableValue<Color>>() {
//                    @Override
//                    public ObservableValue<Color> call(TableColumn.CellDataFeatures<Car, Color> param) {
//                        if (param.getValue().getCarClass() == null) {
//                            return new SimpleObjectProperty<Color>(null);
//                        }
//                        return new SimpleObjectProperty<Color>(param.getValue().getCarClass().getClassColor());
//                    }
//                }
//        );
//        colClassColor.setCellFactory(new Callback<TableColumn<Car, Color>, TableCell<Car, Color>>() {
//            @Override
//            public TableCell<Car, Color> call(TableColumn<Car, Color> column) {
//                return new ColorTableCell<Car>(column);
//            }
//        });
//        tblCars.setItems(listCars);
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
        txtBonusPoints.textProperty().bindBidirectional(configuration.pointStructureProperty().get(0).points, new NumberStringConverter());
        tblPointStructure.setItems(configuration.pointStructureProperty().filtered(pointStructureItem -> pointStructureItem.getFinishPosition() > 0));

        // Drivers (and teams, and cars, oh my!)
        tblDrivers.setItems(configuration.participantConfigurationProperty());
    }

    private void populateDrivers() {
        File[] files = new File(txtSourceTelemetry.getText()).listFiles((dir, name) -> name.matches(".*pdata.*"));
        Arrays.sort(files, (file1, file2) -> {
            Integer n1 = Integer.valueOf(file1.getName().replaceAll("[^\\d]", ""));
            Integer n2 = Integer.valueOf(file2.getName().replaceAll("[^\\d]", ""));
            return Integer.compare(n1, n2);
        });

        Set<String> names = new TreeSet<>();

        for (File file : files) {
            if (file.length() == 1347) {
                try {
                    ParticipantPacket packet = new ParticipantPacket(
                            ByteBuffer.wrap(Files.readAllBytes(file.toPath())));
                    for (SimpleStringProperty name : packet.getNames()) {
                        String trimmedName = name.get().trim();
                        if (trimmedName.length() > 0) {
                            names.add(trimmedName);
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else if (file.length() == 1028) {
                try {
                    AdditionalParticipantPacket packet = new AdditionalParticipantPacket(
                        ByteBuffer.wrap(Files.readAllBytes(file.toPath())));
                    for (SimpleStringProperty name : packet.getNames()) {
                        String trimmedName = name.get().trim();
                        if (trimmedName.length() > 0) {
                            names.add(trimmedName);
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else if (file.length() != 1367) {
                System.out.println(file.length());
            }
        }

        configuration.setParticipantConfiguration(names
                .stream()
                .filter(name -> name.length() > 0)
                .map(Driver::new)
                .collect(Collectors.toCollection(FXCollections::observableArrayList))
        );
    }

    /*
     * From http://info.michael-simons.eu/2014/10/27/custom-editor-components-in-javafx-tablecells/
     */
    class ColorTableCell<T> extends TableCell<T, Color> {
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

    /*
    * From http://docs.oracle.com/javafx/2/ui_controls/table-view.htm
    */
    class EditingCell extends TableCell<PointStructureItem, Integer> {
        private TextField textField;
        
        public EditingCell() {
            
        }
        
        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }
        }
        
        @Override
        public void cancelEdit() {
            super.cancelEdit();
            
            setText(getItem().toString());
            setGraphic(null);
        }
        
        @Override
        public void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty);
            
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }
        
        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> arg0,
                        Boolean arg1, Boolean arg2) {
                    if (!arg2) {
                        commitEdit(Integer.valueOf(textField.getText()));
                    }
                }
            });
        }
        
        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
}