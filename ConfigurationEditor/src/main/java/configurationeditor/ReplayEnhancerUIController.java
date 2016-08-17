/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurationeditor;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author SenorPez
 */
public class ReplayEnhancerUIController implements Initializable {
    ObservableList<PointStructureItem> pointStructure = FXCollections.observableArrayList();
    ObservableSet<Driver> drivers = FXCollections.observableSet();
    ObservableList<Driver> listDrivers = FXCollections.observableArrayList();

    ObservableList<Driver> additionalDrivers = FXCollections.observableArrayList();
    ObservableList<Car> cars = FXCollections.observableArrayList();

    File JSONFile = null;
    
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
    private Label txtFileName;
    
    @FXML
    private Label txtStatusBar;
    
    @FXML
    private Label txtValidConfig;
    
    @FXML
    private void menuFileNew() {
        JSONFile = null;
        txtFileName.setText("<NONE>");
        resetAll();
    }
    
    @FXML
    private void menuFileNewFrom() {
        File file = chooseJSONFile(root);
        if (file != null && file.isFile()) {
            JSONParser parser = new JSONParser();
            try {
                JSONObject data = (JSONObject) parser.parse(new FileReader(file.getCanonicalPath()));
                setValuesFromJSON(data);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }        
        
        JSONFile = null;
        txtFileName.setText("<NONE>");
    }
    
    @FXML
    private void menuFileOpen() {
        File file = chooseJSONFile(root);
        if (file != null && file.isFile()) {
            JSONParser parser = new JSONParser();
            try {
                JSONObject data = (JSONObject) parser.parse(new FileReader(file.getCanonicalPath()));
                setValuesFromJSON(data);
                
                JSONFile = file;
                txtFileName.setText(file.getName());
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        } else {
            JSONFile = null;
            txtFileName.setText("<NONE>");
        }
    }
    
    @FXML
    private void menuFileSave() {
        if (JSONFile == null) {
            menuFileSaveAs();
        } else {
            JSONObject output = writeJSON();
                    
            try {
                FileWriter file = new FileWriter(JSONFile.getCanonicalFile());
                file.write(output.toJSONString());
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    private void menuFileSaveAs() {
        JSONFile = createJSONFile(root);
        
        if (JSONFile != null) {
            JSONObject output = writeJSON();
            txtFileName.setText(JSONFile.getName());
            
            try {
                FileWriter file = new FileWriter(JSONFile.getCanonicalFile());
                file.write(output.toJSONString());
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            JSONFile = null;
            txtFileName.setText("<NONE>");
        }
    }
    
    @FXML
    private void menuFileExit() {
        closeWindow(root);
    }
    
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
        pointStructure.removeAll(pointStructure);
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

        drivers.removeAll(drivers);
        additionalDrivers.removeAll(additionalDrivers);

        cars.removeAll(cars);
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
        
        File file = fileChooser.showOpenDialog(stage);
        return file;
    }
    
    private static File createJSONFile(Pane root) {
        Stage stage = (Stage) root.getScene().getWindow();
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Configuration File As");
        fileChooser.setInitialDirectory(
            new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("JSON", "*.json"),
            new FileChooser.ExtensionFilter("All Files", "*.*"));      
        
        File file = fileChooser.showSaveDialog(stage);
        return file;  
    }

    private void setValuesFromJSON(JSONObject data) {
        txtSourceVideo.setText(data.get("source_video").toString());
        txtSourceTelemetry.setText(data.get("source_telemetry").toString());
        
        txtVideoStart.setText(data.get("video_skipstart").toString());
        txtVideoEnd.setText(data.get("video_skipend").toString());
        txtVideoSync.setText(data.get("sync_racestart").toString());
        
        txtOutputVideo.setText(data.get("output_video").toString());
        
        txtHeadingFont.setText(data.get("heading_font").toString());
        txtHeadingFontSize.setText(data.get("heading_font_size").toString());
        colorHeadingFontColor.setValue(fromJSONColor((JSONArray) data.get("heading_font_color")));
        
        txtFont.setText(data.get("font").toString());
        txtFontSize.setText(data.get("font_size").toString());
        colorFontColor.setValue(fromJSONColor((JSONArray) data.get("font_color")));
        
        txtMarginWidth.setText(data.get("margin").toString());
        txtColumnMarginWidth.setText(data.get("column_margin").toString());
        txtResultLines.setText(data.get("result_lines").toString());
        
        txtBackdrop.setText(data.get("backdrop").toString());
        txtLogo.setText(data.get("logo").toString());
        txtLogoHeight.setText(data.get("logo_height").toString());
        txtLogoWidth.setText(data.get("logo_width").toString());
        
        colorHeadingColor.setValue(fromJSONColor((JSONArray) data.get("heading_color")));
        txtHeadingLogo.setText(data.get("series_logo").toString());
        txtHeadingText.setText(data.get("heading_text").toString());
        txtSubheadingText.setText(data.get("subheading_text").toString());
        
        pointStructure.removeAll(pointStructure);
        JSONArray pointStructureJSON = (JSONArray) data.get("point_structure");
        int i = 0;
        for (Object points : pointStructureJSON) {
            if (i == 0) {
                txtBonusPoints.setText(points.toString());
            } else {
                pointStructure.add(new PointStructureItem(i, Integer.valueOf(points.toString())));
            }
            i += 1;
        }

        drivers.removeAll(drivers);

        JSONObject driversJSON = (JSONObject) data.get("participant_config");
        Map<String,JSONObject> entries = driversJSON;

        for (Entry<String,JSONObject> entry : entries.entrySet()) {
            Driver driver = new Driver(
                    entry.getKey(),
                    entry.getValue().get("display").toString(),
                    entry.getValue().get("short_display").toString(),
                    entry.getValue().get("car").toString()
            );
            if (entry.getValue().get("team") == null) {
                cbUseTeams.setSelected(false);
            } else {
                cbUseTeams.setSelected(true);
                driver.setTeam(entry.getValue().get("team").toString());
            }

            if (entry.getValue().get("points") == null) {
                cbUsePoints.setSelected(false);
            } else {
                cbUsePoints.setSelected(true);
                driver.setSeriesPoints(Integer.valueOf(entry.getValue().get("points").toString()));
            }
            drivers.add(driver);
        }
        tabDrivers.setDisable(false);
    }
    
    private static Color fromJSONColor(JSONArray input) {
        int red = Integer.valueOf(input.get(0).toString());
        int green = Integer.valueOf(input.get(1).toString());
        int blue = Integer.valueOf(input.get(2).toString());
        
        Color output = Color.rgb(red, green, blue);
        return output;
    }
    
    private static JSONArray toJSONColor(Color input) {
        JSONArray output = new JSONArray();
        output.add((int) input.getRed()*255);
        output.add((int) input.getGreen()*255);
        output.add((int) input.getBlue()*255);
        
        return output;
    }
    
    @FXML
    private JSONObject writeJSON() {
        JSONObject output = new JSONObject();
                      
        output.put("source_video", txtSourceVideo.getText());
        output.put("source_telemetry", txtSourceTelemetry.getText());
        
        output.put("video_skipstart", txtVideoStart.getText());
        output.put("video_skipend", txtVideoEnd.getText());
        output.put("sync_racestart", txtVideoSync.getText());
        
        output.put("output_video", txtOutputVideo.getText());
        
        output.put("heading_font", txtHeadingFont.getText());
        output.put("heading_font_size", txtHeadingFontSize.getText());
        output.put("heading_font_color", toJSONColor(colorHeadingFontColor.getValue()));
        
        output.put("font", txtFont.getText());
        output.put("font_size", txtFontSize.getText());
        output.put("font_color", toJSONColor(colorFontColor.getValue()));
        
        output.put("margin", txtMarginWidth.getText());
        output.put("column_margin", txtColumnMarginWidth.getText());
        output.put("result_lines", txtResultLines.getText());
        
        output.put("backdrop", txtBackdrop.getText());
        output.put("logo", txtLogo.getText());
        output.put("logo_height", txtLogoHeight.getText());
        output.put("logo_width", txtLogoWidth.getText());
        
        output.put("heading_color", toJSONColor(colorHeadingColor.getValue()));
        output.put("series_logo", txtHeadingLogo.getText());
        output.put("heading_text", txtHeadingText.getText());
        output.put("subheading_text", txtSubheadingText.getText());
        
        JSONArray pointStructureJSON = new JSONArray();
        pointStructureJSON.add(Integer.valueOf(txtBonusPoints.getText()));
        for (PointStructureItem points : pointStructure) {
            pointStructureJSON.add(points.getPoints());
        }
        output.put("point_structure", pointStructureJSON);
        
        JSONObject driversJSON = new JSONObject();
        
        for (Driver driver : drivers) {
            JSONObject driverDataJSON = new JSONObject();
            driverDataJSON.put("display", driver.getDisplayName());
            driverDataJSON.put("short_display", driver.getShortName());
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
        
        output.put("participant_config", driversJSON);
        return output;
    }
    
    private static void closeWindow(Pane root) {
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

    class DriverUpdater<Driver extends configurationeditor.Driver> implements SetChangeListener {
        @Override
        public void onChanged(Change change) {
            if (change.wasRemoved()) {
                listDrivers.remove(change.getElementRemoved());
            } else if (change.wasAdded()) {
                Driver newDriver = (Driver) change.getElementAdded();
                listDrivers.add(newDriver);
            }

            listDrivers.sort(
                    new Comparator<configurationeditor.Driver>() {
                        @Override
                        public int compare(configurationeditor.Driver o1, configurationeditor.Driver o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    }
            );
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

        drivers.addListener(new DriverUpdater<Driver>());
        
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
        tblDrivers.setItems(listDrivers);
    }
    
    private ObservableSet<Driver> populateDrivers() {
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
        
        Set<String> names = new TreeSet<String>(
                new Comparator<String>() {
                    @Override
                    public int compare(String driver1, String driver2) {
                        return driver1.compareTo(driver2);
                    }
                }
        );

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