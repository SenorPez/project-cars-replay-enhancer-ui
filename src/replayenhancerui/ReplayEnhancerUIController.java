/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package replayenhancerui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author 502625185
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
    private void validateInteger(ActionEvent event) {
        //TODO
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        Object source = event.getSource();
        Button btnSource = (Button) source;
        Stage stage = (Stage) root.getScene().getWindow();
        

        if (btnSource.getId().equals("btnSourceVideo")) {
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
            if (file.isFile()) {
                try {
                    String sourceVideo = file.getCanonicalPath();
                    txtSourceVideo.setText(sourceVideo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
        } else if (btnSource.getId().equals("btnSourceTelemetry")) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Open Source Telemetry Directory");
            directoryChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
            );
            File directory = directoryChooser.showDialog(stage);
            if (directory.isDirectory()) {
                try {
                    String sourceTelemetry = directory.getCanonicalPath();
                    txtSourceTelemetry.setText(sourceTelemetry);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (btnSource.getId().equals("btnOutputVideo")) {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
        }
                    
        // System.out.println(btnSource.getId());
        // System.out.println("You clicked me!");
        // label.setText("Hello World!");
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}