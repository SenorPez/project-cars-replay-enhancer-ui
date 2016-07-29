/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package replayenhancerui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author SenorPez
 */
public class ReplayEnhancerUIController implements Initializable {
    
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
            
            default:
                break;
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}