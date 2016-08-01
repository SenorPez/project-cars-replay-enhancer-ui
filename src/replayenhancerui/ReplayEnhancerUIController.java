/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package replayenhancerui;

import java.io.File;
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
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;


/**
 *
 * @author SenorPez
 */
public class ReplayEnhancerUIController implements Initializable {

    ObservableList<PointStructureItem> pointStructure = FXCollections.observableArrayList();
    ObservableList<Driver> drivers = FXCollections.observableArrayList();

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
    private TextField txtFont;
    
    @FXML
    private TextField txtFontSize;
    
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
    private TextField txtHeadingLogo;
    
    @FXML
    private TextField txtHeadingText;
    
    @FXML
    private TextField txtSubheadingText;
    
    @FXML
    private TableView<PointStructureItem> tblPointStructure;
    
    @FXML
    private TableColumn<PointStructureItem, Integer> colFinishPosition;
    
    @FXML
    private TableColumn<PointStructureItem, Integer> colPoints;
    
    @FXML
    private TextField txtBonusPoints;
    
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
    
    public ObservableList<Driver> populateDrivers() {
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
                    ByteBuffer data = ByteBuffer.wrap(Files.readAllBytes(file.toPath()));
                         
                    byte[] version = new byte[2];
                    data.get(version);
                    
                    byte[] packetType = new byte[1];
                    data.get(packetType);
                    
                    byte[] carName = new byte[64];
                    data.get(carName);
                    
                    byte[] carClass = new byte[64];
                    data.get(carClass);
                    
                    byte[] trackLocation = new byte[64];
                    data.get(trackLocation);
                    
                    byte[] trackVariation = new byte[64];
                    data.get(trackVariation);

                    for (int i=0; i<16; i++) {
                        byte[] byteName = new byte[64];
                        data.get(byteName);
                        names.add(new String(byteName, "UTF-8").trim());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (file.length() == 1028) {
                try {
                    ByteBuffer data = ByteBuffer.wrap(Files.readAllBytes(file.toPath()));
                         
                    byte[] version = new byte[2];
                    data.get(version);
                    
                    byte[] packetType = new byte[1];
                    data.get(packetType);
                    
                    for (int i=0; i<16; i++) {
                        byte[] byteName = new byte[64];
                        data.get(byteName);
                        names.add(new String(byteName, "UTF-8").trim());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
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
    
    public static class Driver {
        private final SimpleStringProperty name;
        private final SimpleStringProperty displayName;
        private final SimpleStringProperty shortName;
        private final SimpleStringProperty car;
        private final SimpleStringProperty team;
        private final SimpleIntegerProperty seriesPoints;
        
        private Driver(String name) {
            this.name = new SimpleStringProperty(name);
            this.displayName = new SimpleStringProperty(name);
            this.shortName = new SimpleStringProperty(name);
            this.car = new SimpleStringProperty("");
            this.team = new SimpleStringProperty("");
            this.seriesPoints = new SimpleIntegerProperty(0);
        }
        
        public String getName() {
            return name.get();
        }
        
        public void setName(String value) {
            name.set(value);
        }
        
        public String getDisplayName() {
            return displayName.get();
        }
        
        public void setDisplayName(String value) {
            displayName.set(value);
        }
        
        public String getShortName() {
            return shortName.get();
        }
        
        public void setShortName(String value) {
            shortName.set(value);
        }
        
        public String getCar() {
            return car.get();
        }
        
        public void setCar(String value) {
            car.set(value);
        }
        
        public String getTeam() {
            return team.get();
        }
        
        public void setTeam(String value) {
            team.set(value);
        }
        
        public Integer getSeriesPoints() {
            return seriesPoints.get();
        }
        
        public void setSeriesPoints(Integer value) {
            seriesPoints.set(value);
        }
    }
    
    public static class PointStructureItem {
        private final SimpleIntegerProperty finishPosition;
        private final SimpleIntegerProperty points;
        
        private PointStructureItem(Integer finishPosition, Integer points) {
            this.finishPosition = new SimpleIntegerProperty(finishPosition);
            this.points = new SimpleIntegerProperty(points);
        }
        
        public Integer getFinishPosition() {
            return finishPosition.get();
        }
        
        public void setFinishPosition(Integer value) {
            finishPosition.set(value);
        }
        
        public Integer getPoints() {
            return points.get();
        }
        
        public void setPoints(Integer value) {
            points.set(value);
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