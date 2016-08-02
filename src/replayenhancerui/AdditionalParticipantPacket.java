/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package replayenhancerui;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author SenorPez
 */
public class AdditionalParticipantPacket extends Packet {
    
    public AdditionalParticipantPacket(ByteBuffer data) throws UnsupportedEncodingException {
        this.buildVersionNumber = new SimpleIntegerProperty(ReadShort(data));
        this.packetType = new SimpleIntegerProperty(ReadChar(data));            
            
        this.names = null;
            
        for (int i=0; i<16; i++) {
            this.names.add(new SimpleStringProperty(ReadString(data, 64)));
        }
    }
}