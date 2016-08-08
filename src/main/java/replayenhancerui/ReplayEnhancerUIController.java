/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package replayenhancerui;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


/**
 *
 * @author SenorPez
 */
public class ReplayEnhancerUIController implements Initializable {

    ObservableList<PointStructureItem> pointStructure = FXCollections.observableArrayList();
    ObservableList<Driver> drivers = FXCollections.observableArrayList();
    ObservableList<Driver> additionalDrivers = FXCollections.observableArrayList();
    ObservableSet<Car> cars = FXCollections.observableSet();
    
    File JSONFile = new File("C:/Users/502625185/Downloads/out.json");
    
    @FXML
    private MenuItem fileNew;
    
    @FXML
    private MenuItem fileSave;
    
    @FXML
    private MenuItem fileClose;
    
    @FXML
    private VBox root;
    
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
    private CheckBox cbUseTeams;
    
    @FXML
    private CheckBox cbUseClasses;
    
    @FXML
    private CheckBox cbShowChampion;
    
    @FXML
    private CheckBox cbUsePoints;
        
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
    private Label txtStatusBar;
    
    @FXML
    private Label txtValidConfig;
    
    @FXML
    private void resetAll() {
        txtSourceVideo.setText("");
        txtSourceTelemetry.setText("");
        txtVideoStart.setText("");
        txtVideoEnd.setText("");
        txtVideoSync.setText("");
        txtOutputVideo.setText("");
        
        txtHeadingFont.setText("");
        txtHeadingFontSize.setText("");
        colorHeadingFontColor.setValue(Color.WHITE);
        
        txtFont.setText("");
        txtFontSize.setText("");
        colorFontColor.setValue(Color.BLACK);
        
        txtMarginWidth.setText("");
        txtColumnMarginWidth.setText("");
        txtResultLines.setText("");
        
        txtBackdrop.setText("");
        txtLogo.setText("");
        txtLogoWidth.setText("");
        txtLogoHeight.setText("");
        
        colorHeadingColor.setValue(Color.BLACK);
        txtHeadingLogo.setText("");
        txtHeadingText.setText("");
        txtSubheadingText.setText("");
        
        cbUseTeams.setSelected(true);
        cbUseClasses.setSelected(false);
        cbShowChampion.setSelected(false);
        
        txtBonusPoints.setText("0");
        pointStructure.add(
            new PointStructureItem(1, 25));
        pointStructure.add(
            new PointStructureItem(2, 18));
        pointStructure.add(
            new PointStructureItem(3, 15));
        pointStructure.add(
            new PointStructureItem(4, 12));
        pointStructure.add(
            new PointStructureItem(5, 10));
        pointStructure.add(
            new PointStructureItem(6, 8));
        pointStructure.add(
            new PointStructureItem(7, 6));
        pointStructure.add(
            new PointStructureItem(8, 4));
        pointStructure.add(
            new PointStructureItem(9, 2));
        pointStructure.add(
            new PointStructureItem(10, 1));
        
        drivers.clear();
        additionalDrivers.clear();
        
        cars.clear();
    }
    
    private Integer[] convertColor(Color input) {
        Integer[] output = new Integer[3];
        output[0] = (int) input.getRed()*255;
        output[1] = (int) input.getGreen()*255;
        output[2] = (int) input.getBlue()*255;
        
        return output;
    }
    
    private JSONArray JSONColor(Color input) {
        JSONArray output = new JSONArray();
        output.add((int) input.getRed()*255);
        output.add((int) input.getGreen()*255);
        output.add((int) input.getBlue()*255);
        
        return output;
    }
    
    @FXML
    private void writeJSON() {
        JSONObject output = new JSONObject();
                      
        output.put("source_video", txtSourceVideo.getText());
        output.put("source_telemetry", txtSourceTelemetry.getText());
        
        output.put("skip_start", txtVideoStart.getText());
        output.put("skip_end", txtVideoEnd.getText());
        output.put("racesync", txtVideoSync.getText());
        
        output.put("output_video", txtOutputVideo.getText());
        
        output.put("heading_font", txtHeadingFont.getText());
        output.put("heading_font_size", txtHeadingFontSize.getText());
        output.put("heading_font_color", JSONColor(colorHeadingFontColor.getValue()));
        
        output.put("font", txtFont.getText());
        output.put("font_size", txtFontSize.getText());
        output.put("font_color", JSONColor(colorFontColor.getValue()));
        
        output.put("margin", txtMarginWidth.getText());
        output.put("column_margin", txtColumnMarginWidth.getText());
        output.put("result_lines", txtResultLines.getText());
        
        output.put("backdrop", txtBackdrop.getText());
        output.put("logo", txtLogo.getText());
        output.put("logo_height", txtLogoHeight.getText());
        output.put("logo_width", txtLogoWidth.getText());
        
        output.put("heading_color", JSONColor(colorHeadingColor.getValue()));
        output.put("heading_logo", txtHeadingLogo.getText());
        output.put("heading_text", txtHeadingText.getText());
        output.put("subheading_text", txtSubheadingText.getText());
        
        JSONArray pointStructureJSON = new JSONArray();
        pointStructureJSON.add(Integer.valueOf(txtBonusPoints.getText()));
        for (PointStructureItem points : pointStructure) {
            pointStructureJSON.add(points.getPoints());
        }
        output.put("points_structure", pointStructureJSON);
        
        JSONObject driversJSON = new JSONObject();
        
        for (Driver driver : drivers) {
            JSONObject driverDataJSON = new JSONObject();
            driverDataJSON.put("name_display", driver.getDisplayName());
            driverDataJSON.put("short_name_display", driver.getShortName());
            driverDataJSON.put("car", driver.getCar());
    
            if (cbUseTeams.isSelected()) {
                driverDataJSON.put("team", driver.getTeam());
            } else {
                driverDataJSON.put("team", null);
            }
            
            if (cbUsePoints.isSelected()) {
                driverDataJSON.put("points", driver.getSeriesPoints());
            } else {
                driverDataJSON.put("points", null);
            }
            driversJSON.put(driver.getName(), driverDataJSON);
        }
        
        output.put("participant_data", driversJSON);
        
        try {
            FileWriter file = new FileWriter("C:\\Users\\502625185\\Downloads\\out.json");
            file.write(output.toJSONString());
            file.flush();
            file.close();
        
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println(output.toJSONString());
    }
    
    @FXML
    private void closeWindow() {
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
    private void validateFloat(KeyEvent event) {
        Object source = event.getSource();
        TextField txtSource = (TextField) source;
        txtSource.setStyle("-fx-text-inner-color: black");
        
        try {
            Float value = Float.valueOf(txtSource.getText());
        } catch (NumberFormatException e) {
            if (!txtSource.getText().equals("")) {
                txtSource.setStyle("-fx-text-inner-color: red");                        
            }
        }
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        Object source = event.getSource();
        Button btnSource = (Button) source;
        Stage stage = (Stage) root.getScene().getWindow();

        switch (btnSource.getId()) {
            case "btnSourceVideo": {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Source Video File");
                fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home"))
                );      
                fileChooser.getExtensionFilters().addAll(

                    new FileChooser.ExtensionFilter("MP4", "*.mp4"),
                    new FileChooser.ExtensionFilter("AVI", "*.avi"),
                    new FileChooser.ExtensionFilter("All Files", "*.*")
                );      
                File file = fileChooser.showOpenDialog(stage);
                if (file != null && file.isFile()) {
                    try {
                        String sourceVideo = file.getCanonicalPath();
                        txtSourceVideo.setText(sourceVideo);
                    } catch (IOException e) {
                        throw(e);
                    }
                }
                break;
            }
        
            case "btnSourceTelemetry": {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Open Source Telemetry Directory");
                directoryChooser.setInitialDirectory(
                    new File(System.getProperty("user.home"))
                );  
                File directory = directoryChooser.showDialog(stage);
                if (directory != null && directory.isDirectory()) {
                    try {
                        String sourceTelemetry = directory.getCanonicalPath();
                        txtSourceTelemetry.setText(sourceTelemetry);
                        populateDrivers();
                    } catch (IOException e) {
                        throw(e);
                    }
                }   
                break;
            }
            
            case "btnOutputVideo": {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Output Video File");
                fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home"))
                );
                fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("MP4", "*.mp4"),
                    new FileChooser.ExtensionFilter("AVI", "*.avi"),
                    new FileChooser.ExtensionFilter("All Files", "*.*")
                );      
                File file = fileChooser.showSaveDialog(stage);
                if (file != null) {
                    try {
                        String outputVideo = file.getCanonicalPath();
                        txtOutputVideo.setText(outputVideo);
                    } catch (IOException e) {
                        throw(e);
                    }
                }       
                break;
            }
            
            case "btnHeadingFont": {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select Heading Font");
                fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home"))
                );
                fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("TTF", "*.ttf"),
                    new FileChooser.ExtensionFilter("All Files", "*.*")
                );
                File file = fileChooser.showOpenDialog(stage);
                if (file != null && file.isFile()) {
                    try {
                        String headingFont = file.getCanonicalPath();
                        txtHeadingFont.setText(headingFont);
                    } catch (IOException e) {
                        throw(e);
                    }
                }
                break;
            }
            
            case "btnFont": {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select Font");
                fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home"))
                );
                fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("TTF", "*.ttf"),
                    new FileChooser.ExtensionFilter("All Files", "*.*")
                );
                File file = fileChooser.showOpenDialog(stage);
                if (file != null && file.isFile()) {
                    try {
                        String font = file.getCanonicalPath();
                        txtFont.setText(font);
                    } catch (IOException e) {
                        throw(e);
                    }
                }
                break;
            }
            
            case "btnBackdrop": {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Backdrop Image");
                fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home"))
                );      
                fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("PNG", "*.png"),
                    new FileChooser.ExtensionFilter("All Files", "*.*")
                );      
                File file = fileChooser.showOpenDialog(stage);
                if (file != null && file.isFile()) {
                    try {
                        String sourceVideo = file.getCanonicalPath();
                        txtBackdrop.setText(sourceVideo);
                    } catch (IOException e) {
                        throw(e);
                    }
                }
                break;
            }            
            
            case "btnLogo": {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Logo Image");
                fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home"))
                );      
                fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PNG", "*.png"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("All Files", "*.*")
                );      
                File file = fileChooser.showOpenDialog(stage);
                if (file != null && file.isFile()) {
                    try {
                        String sourceVideo = file.getCanonicalPath();
                        txtLogo.setText(sourceVideo);
                    } catch (IOException e) {
                        throw(e);
                    }
                }
                break;
            }     
            
            case "btnHeadingLogo": {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Heading Logo Image");
                fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home"))
                );      
                fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("PNG", "*.png"),
                    new FileChooser.ExtensionFilter("All Files", "*.*")
                );      
                File file = fileChooser.showOpenDialog(stage);
                if (file != null && file.isFile()) {
                    try {
                        String sourceVideo = file.getCanonicalPath();
                        txtHeadingLogo.setText(sourceVideo);
                    } catch (IOException e) {
                        throw(e);
                    }
                }
                break;
            }            
            
            case "btnAddPosition": {
                pointStructure.add(new PointStructureItem(
                    pointStructure.size()+1, 0)
                );
                break;
            }            
            
            default:
                break;
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        Callback<TableColumn<PointStructureItem, Integer>, TableCell<PointStructureItem, Integer>> cellFactory = 
//            new Callback<TableColumn<PointStructureItem, Integer>, TableCell<PointStructureItem, Integer>>() {
//                public TableCell call(TableColumn p) {
//                    return new EditingCell();
//                }
//            };
        resetAll();
        
        colFinishPosition.setCellValueFactory(
            new PropertyValueFactory<PointStructureItem,Integer>("finishPosition")
        );
        colPoints.setCellValueFactory(
            new PropertyValueFactory<PointStructureItem,Integer>("points")
        );
        colPoints.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colPoints.setOnEditCommit(
            new EventHandler<CellEditEvent<PointStructureItem, Integer>>() {
                @Override
                public void handle(CellEditEvent<PointStructureItem, Integer> t) {
                    ((PointStructureItem) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                            ).setPoints(t.getNewValue());
                }
            }
        );
        tblPointStructure.setItems(pointStructure);
        
        colName.setCellValueFactory(
            new PropertyValueFactory<Driver, String>("name")
        );
        colDisplayName.setCellValueFactory(
            new PropertyValueFactory<Driver, String>("displayName")
        );
        colDisplayName.setCellFactory(TextFieldTableCell.forTableColumn());
        colDisplayName.setOnEditCommit(
            new EventHandler<CellEditEvent<Driver, String>>() {
                @Override
                public void handle(CellEditEvent<Driver, String> t) {
                    ((Driver) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                            ).setName(t.getNewValue());
                }
            }
        );
        colShortName.setCellValueFactory(
            new PropertyValueFactory<Driver, String>("shortName")
        );
        colShortName.setCellFactory(TextFieldTableCell.forTableColumn());
        colShortName.setOnEditCommit(
            new EventHandler<CellEditEvent<Driver, String>>() {
                @Override
                public void handle(CellEditEvent<Driver, String> t) {
                    ((Driver) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                            ).setShortName(t.getNewValue());
                }
            }
        );        
        colCar.setCellValueFactory(
            new PropertyValueFactory<Driver, String>("car")
        );
        colCar.setCellFactory(TextFieldTableCell.forTableColumn());
        colCar.setOnEditCommit(
            new EventHandler<CellEditEvent<Driver, String>>() {
                @Override
                public void handle(CellEditEvent<Driver, String> t) {
                    ((Driver) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                            ).setCar(t.getNewValue());
                }
            }
        );                
        colTeam.setCellValueFactory(
            new PropertyValueFactory<Driver, String>("team")
        );
        colTeam.setCellFactory(TextFieldTableCell.forTableColumn());
        colTeam.setOnEditCommit(
            new EventHandler<CellEditEvent<Driver, String>>() {
                @Override
                public void handle(CellEditEvent<Driver, String> t) {
                    ((Driver) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                            ).setTeam(t.getNewValue());
                }
            }
        );        
        colSeriesPoints.setCellValueFactory(
            new PropertyValueFactory<Driver, Integer>("seriesPoints")
        );
        colSeriesPoints.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colSeriesPoints.setOnEditCommit(
            new EventHandler<CellEditEvent<Driver, Integer>>() {
                @Override
                public void handle(CellEditEvent<Driver, Integer> t) {
                    ((Driver) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                            ).setSeriesPoints(t.getNewValue());
                }
            }
        );
        tblDrivers.setItems(populateDrivers());
        
    }
    
    private ObservableList<Driver> populateDrivers() {
        File testFile = new File(txtSourceTelemetry.getText());
        
        if (!testFile.isDirectory()) {
            return drivers;
        }
        
        File[] files = new File(txtSourceTelemetry.getText()).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.matches(".*pdata.*");
            }
        });
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                Integer n1 = Integer.valueOf(o1.getName().replaceAll("[^\\d]", ""));
                Integer n2 = Integer.valueOf(o2.getName().replaceAll("[^\\d]", ""));
                return Integer.compare(n1, n2);
            }
        });
        
        Set<String> names = new TreeSet<String>();
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
                    Logger.getLogger(Packet.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(Packet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        for (String name : names) {
            if (name.length() > 0) {
                drivers.add(new Driver(name));
            }
        }        
        return drivers;
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