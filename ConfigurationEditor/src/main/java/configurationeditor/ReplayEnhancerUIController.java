/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurationeditor;

import javafx.beans.property.SimpleObjectProperty;
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
import javafx.util.Callback;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * @author SenorPez
 */
public class ReplayEnhancerUIController implements Initializable {
    private ObservableMap<Integer, PointStructureItem> pointStructure = FXCollections.observableMap(new TreeMap<>());
    private ObservableList<PointStructureItem> listPointStructure = FXCollections.observableArrayList();

    private ObservableSet<Driver> drivers = FXCollections.observableSet();
    private ObservableList<Driver> listDrivers = FXCollections.observableArrayList();

    private ObservableList<Driver> additionalDrivers = FXCollections.observableArrayList();

    private ObservableSet<Car> cars = FXCollections.observableSet();
    private ObservableList<Car> listCars = FXCollections.observableArrayList();

    private File JSONFile = null;
    
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
            } catch (IOException | ParseException ex) {
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
            } catch (IOException | ParseException ex) {
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
        pointStructure.clear();
        pointStructure.put(1, new PointStructureItem(1, 25));
        pointStructure.put(2, new PointStructureItem(2, 18));
        pointStructure.put(3, new PointStructureItem(3, 15));
        pointStructure.put(4, new PointStructureItem(4, 12));
        pointStructure.put(5, new PointStructureItem(5, 10));
        pointStructure.put(6, new PointStructureItem(6, 8));
        pointStructure.put(7, new PointStructureItem(7, 6));
        pointStructure.put(8, new PointStructureItem(8, 4));
        pointStructure.put(9, new PointStructureItem(9, 2));
        pointStructure.put(10, new PointStructureItem(10, 1));

        drivers.clear();
        additionalDrivers.clear();

        cars.clear();
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
    
    private static File createJSONFile(Pane root) {
        Stage stage = (Stage) root.getScene().getWindow();
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Configuration File As");
        fileChooser.setInitialDirectory(
            new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("JSON", "*.json"),
            new FileChooser.ExtensionFilter("All Files", "*.*"));

        return fileChooser.showSaveDialog(stage);
    }

    private void setValuesFromJSON(JSONObject data) {
        txtSourceVideo.setText(data.get("source_video").toString());
        txtSourceTelemetry.setText(data.get("source_telemetry").toString());

        txtVideoStart.setText(convertTime((double) data.get("video_skipstart")));
        txtVideoEnd.setText(convertTime((double) data.get("video_skipend")));
        txtVideoSync.setText(convertTime((double) data.get("sync_racestart")));
        
        txtOutputVideo.setText(data.get("output_video").toString());
        
        txtHeadingFont.setText(data.get("heading_font").toString());
        txtHeadingFontSize.setText(data.get("heading_font_size").toString());
        colorHeadingFontColor.setValue(fromJSONColor((JSONArray) data.get("heading_font_color")));
        
        txtFont.setText(data.get("font").toString());
        txtFontSize.setText(data.get("font_size").toString());
        colorFontColor.setValue(fromJSONColor((JSONArray) data.get("font_color")));
        
        txtMarginWidth.setText(data.get("margin").toString());
        txtColumnMarginWidth.setText(data.get("column_margin").toString());

        if (data.get("result_lines") == null) {
            txtResultLines.setText("");
        } else {
            txtResultLines.setText(data.get("result_lines").toString());
        }

        txtBackdrop.setText(data.get("backdrop").toString());
        txtLogo.setText(data.get("logo").toString());
        txtLogoHeight.setText(data.get("logo_height").toString());
        txtLogoWidth.setText(data.get("logo_width").toString());
        
        colorHeadingColor.setValue(fromJSONColor((JSONArray) data.get("heading_color")));
        txtHeadingLogo.setText(data.get("series_logo").toString());
        txtHeadingText.setText(data.get("heading_text").toString());
        txtSubheadingText.setText(data.get("subheading_text").toString());
        
        pointStructure.clear();
        JSONArray pointStructureJSON = (JSONArray) data.get("point_structure");
        Integer i = 0;
        for (Object points : pointStructureJSON) {
            if (i == 0) {
                txtBonusPoints.setText(points.toString());
            } else {
                pointStructure.put(i, new PointStructureItem(i, Integer.valueOf(points.toString())));
            }
            i += 1;
        }

        drivers.clear();

        Map<String,JSONObject> entries = (JSONObject) data.get("participant_config");

        Map<String,JSONObject> carClassEntries = (JSONObject) data.get("car_classes");

        for (Entry<String,JSONObject> entry : entries.entrySet()) {
            Car car = createCar(entry.getValue().get("car").toString(), carClassEntries);
            cars.add(car);

            Driver driver = new Driver(
                    entry.getKey(),
                    entry.getValue().get("display").toString(),
                    entry.getValue().get("short_display").toString(),
                    car
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

    private Car createCar(String carName, Map<String,JSONObject> carClasses) {
        if (carClasses == null) {
            return new Car(carName);
        } else {
            for (Entry<String, JSONObject> entry : carClasses.entrySet()) {
                JSONArray carsInClass = (JSONArray) entry.getValue().get("cars");
                if (carsInClass.contains(carName)) {
                    return new Car(carName, new CarClass(
                            entry.getKey(), fromJSONColor((JSONArray) entry.getValue().get("color"))
                    ));
                }
            }
            return new Car(carName);
        }
    }
    
    private static Color fromJSONColor(JSONArray input) {
        int red = Integer.valueOf(input.get(0).toString());
        int green = Integer.valueOf(input.get(1).toString());
        int blue = Integer.valueOf(input.get(2).toString());

        return Color.rgb(red, green, blue);
    }
    
    private static JSONArray toJSONColor(Color input) {
        JSONArray output = new JSONArray();
        output.add((int) (input.getRed() * 255));
        output.add((int) (input.getGreen() * 255));
        output.add((int) (input.getBlue() * 255));
        
        return output;
    }

    private String convertTime(double dblTime) {
        String returnValue = "";
        if ((int) (dblTime % 1) > 0) {
            returnValue = "." + String.format(".%d", (int) (dblTime % 1));
        }

        if ((int) (dblTime / 3600) > 0) {
            returnValue = String.format("%d", (int) (dblTime / 3600)) + ":" + String.format("%02d", (int) ((dblTime % 3600) / 60)) + ":" + String.format("%02d", (int) (dblTime % 60)) + returnValue;
        } else {
            if ((int) ((dblTime % 3600) / 60) > 0) {
                returnValue = String.format("%d", (int) ((dblTime % 3600) / 60)) + ":" + String.format("%02d", (int) (dblTime % 60)) + returnValue;
            } else {
                returnValue = "0:" + String.format("%02d", (int) (dblTime % 60)) + returnValue;
            }
        }

        return returnValue;
    }

    private Float convertTime(String strTime) {
        Pattern regex = Pattern.compile("(?:^(\\d*):([0-5]?\\d):([0-5]?\\d(?:\\.\\d*)?)$|^(\\d*):([0-5]?\\d(?:\\.\\d*)?)$|^(\\d*(?:\\.\\d*)?)$)");
        Matcher matches = regex.matcher(strTime);
        matches.matches();
        Float hours = 0f;
        Float minutes = 0f;
        Float seconds = 0f;

        if (matches.group(1) != null) {
            hours = Float.valueOf(matches.group(1)) * 60 * 60;
            minutes = Float.valueOf(matches.group(2)) * 60;
            seconds = Float.valueOf(matches.group(3));
        } else if (matches.group(4) != null) {
            minutes = Float.valueOf(matches.group(4)) * 60;
            seconds = Float.valueOf(matches.group(5));
        } else if (matches.group(6) != null) {
            seconds = Float.valueOf(matches.group(6));
        }

        return hours + minutes + seconds;
    }
    
    @FXML
    private JSONObject writeJSON() {
        JSONObject output = new JSONObject();
                      
        output.put("source_video", txtSourceVideo.getText());
        output.put("source_telemetry", txtSourceTelemetry.getText());

        output.put("video_skipstart", convertTime(txtVideoStart.getText()));
        output.put("video_skipend", convertTime(txtVideoEnd.getText()));
        output.put("sync_racestart", convertTime(txtVideoSync.getText()));
        
        output.put("output_video", txtOutputVideo.getText());
        
        output.put("heading_font", txtHeadingFont.getText());
        output.put("heading_font_size", Integer.valueOf(txtHeadingFontSize.getText()));
        output.put("heading_font_color", toJSONColor(colorHeadingFontColor.getValue()));
        
        output.put("font", txtFont.getText());
        output.put("font_size", Integer.valueOf(txtFontSize.getText()));
        output.put("font_color", toJSONColor(colorFontColor.getValue()));
        
        output.put("margin", Integer.valueOf(txtMarginWidth.getText()));
        output.put("column_margin", Integer.valueOf(txtColumnMarginWidth.getText()));
        output.put("result_lines", Integer.valueOf(txtResultLines.getText()));
        
        output.put("backdrop", txtBackdrop.getText());
        output.put("logo", txtLogo.getText());
        output.put("logo_height", Integer.valueOf(txtLogoHeight.getText()));
        output.put("logo_width", Integer.valueOf(txtLogoWidth.getText()));
        
        output.put("heading_color", toJSONColor(colorHeadingColor.getValue()));
        output.put("series_logo", txtHeadingLogo.getText());
        output.put("heading_text", txtHeadingText.getText());
        output.put("subheading_text", txtSubheadingText.getText());
        
        JSONArray pointStructureJSON = new JSONArray();
        pointStructureJSON.add(Integer.valueOf(txtBonusPoints.getText()));
        for (Map.Entry<Integer, PointStructureItem> entry : pointStructure.entrySet()) {
            pointStructureJSON.add(entry.getValue().getPoints());
        }
        output.put("point_structure", pointStructureJSON);
        
        JSONObject driversJSON = new JSONObject();
        
        for (Driver driver : drivers) {
            JSONObject driverDataJSON = new JSONObject();
            driverDataJSON.put("display", driver.getDisplayName());
            driverDataJSON.put("short_display", driver.getShortName());
            driverDataJSON.put("car", driver.getCarName());
    
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
    private void removePosition(ActionEvent event){
        ObservableList<PointStructureItem> selectedItems = tblPointStructure.getSelectionModel().getSelectedItems();
        for (PointStructureItem item : selectedItems) {
            pointStructure.remove(item.getFinishPosition());
        }

        Iterator<Map.Entry<Integer, PointStructureItem>> iterator = pointStructure.entrySet().iterator();
        Integer index = 1;
        TreeMap<Integer, PointStructureItem> newItems = new TreeMap<>();
        while (iterator.hasNext()) {
            Map.Entry<Integer, PointStructureItem> entry = iterator.next();
            if (!entry.getKey().equals(index)) {
                PointStructureItem old_item = entry.getValue();
                old_item.setFinishPosition(index);
                newItems.put(index, old_item);
                iterator.remove();
            }
            index += 1;
        }
        pointStructure.putAll(newItems);
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
                        tabDrivers.setDisable(false);
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
                int next_index = pointStructure.size() + 1;
                pointStructure.put(next_index, new PointStructureItem(next_index, 0));
                break;
            }            
            
            default:
                break;
        }
    }

    class PointsUpdater implements MapChangeListener<Integer, PointStructureItem> {
        @Override
        public void onChanged(Change<? extends Integer, ? extends PointStructureItem> change) {
            if (change.wasRemoved()) {
                listPointStructure.remove(change.getValueRemoved());
            }

            if (change.wasAdded()) {
                PointStructureItem newPoints = change.getValueAdded();
                listPointStructure.add(newPoints);
            }

            listPointStructure.sort(
                    new Comparator<PointStructureItem>() {
                        @Override
                        public int compare(PointStructureItem o1, PointStructureItem o2) {
                            return o1.getFinishPosition().compareTo(o2.getFinishPosition());
                        }
                    }
            );
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

    class CarUpdater<Car extends configurationeditor.Car> implements SetChangeListener {
        @Override
        public void onChanged(Change change) {
            if (change.wasRemoved()) {
                listCars.remove(change.getElementRemoved());
            } else if (change.wasAdded()) {
                Car newCar = (Car) change.getElementAdded();
                listCars.add(newCar);
            }

            listCars.sort(
                    new Comparator<configurationeditor.Car>() {
                        @Override
                        public int compare(configurationeditor.Car o1, configurationeditor.Car o2) {
                            return o1.getCarName().compareTo(o2.getCarName());
                        }
                    }
            );
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pointStructure.addListener(new PointsUpdater());
        drivers.addListener(new DriverUpdater<Driver>());
        cars.addListener(new CarUpdater<Car>());
        
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
        tblPointStructure.setItems(listPointStructure);

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
                new Callback<TableColumn.CellDataFeatures<Driver, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Driver, String> param) {
                        Car car = param.getValue().getCar();
                        if (car == null) {
                            return new SimpleStringProperty("");
                        } else {
                            return new SimpleStringProperty(car.getCarName());
                        }
                    };
                }
        );
        colCar.setCellFactory(TextFieldTableCell.forTableColumn());
        colCar.setOnEditCommit(
            new EventHandler<CellEditEvent<Driver, String>>() {
                @Override
                public void handle(CellEditEvent<Driver, String> t) {
                    ((Driver) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                            ).setCar(new Car(t.getNewValue()));
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

        colCarName.setCellValueFactory(
                new PropertyValueFactory<Car, String>("carName")
        );
        colCarName.setCellFactory(TextFieldTableCell.forTableColumn());
        colCarName.setOnEditCommit(
                new EventHandler<CellEditEvent<Car, String>>() {
                    @Override
                    public void handle(CellEditEvent<Car, String> event) {
                        ((Car) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setCarName(event.getNewValue());
                    }
                }
        );

        colClassName.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Car, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Car, String> param) {
                        if (param.getValue().getCarClass() == null) {
                            return new SimpleStringProperty("");
                        }
                        return new SimpleStringProperty(param.getValue().getCarClass().getClassName());
                    }
                }
        );
        colClassName.setCellFactory(TextFieldTableCell.forTableColumn());
        colClassName.setOnEditCommit(
                new EventHandler<CellEditEvent<Car, String>>() {
                    @Override
                    public void handle(CellEditEvent<Car, String> event) {
                        ((Car) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setCarName(event.getNewValue());
                    }
                }
        );
        colClassColor.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Car, Color>, ObservableValue<Color>>() {
                    @Override
                    public ObservableValue<Color> call(TableColumn.CellDataFeatures<Car, Color> param) {
                        if (param.getValue().getCarClass() == null) {
                            return new SimpleObjectProperty<Color>(null);
                        }
                        return new SimpleObjectProperty<Color>(param.getValue().getCarClass().getClassColor());
                    }
                }
        );
        colClassColor.setCellFactory(new Callback<TableColumn<Car, Color>, TableCell<Car, Color>>() {
            @Override
            public TableCell<Car, Color> call(TableColumn<Car, Color> column) {
                return new ColorTableCell<Car>(column);
            }
        });
        tblCars.setItems(listCars);

        resetAll();
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

        for (String name : names) {
            if (name.length() > 0) {
                drivers.add(new Driver(name));
            }
        }
        return drivers;
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