/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package packetcapture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author senor
 */
public class PacketCaptureController {
    @FXML
    private AnchorPane root;
    
    @FXML
    private Button btnStartCapture;
    
    @FXML
    private Button btnEndCapture;
    
    @FXML
    private TextArea txtOutput;
    
    public void initialize() {
        resetAll();
    }
    
    @FXML
    private void resetAll() {
        btnStartCapture.setDisable(false);
        btnStartCapture.setDefaultButton(true);
        btnEndCapture.setDisable(true);
        txtOutput.setEditable(false);
        txtOutput.setText("Ready to capture...");
    }
            
}
