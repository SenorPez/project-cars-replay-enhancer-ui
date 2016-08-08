/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package replayenhancerui;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author SenorPez
 */
public class AdditionalParticipantPacket extends Packet {
    private final SimpleIntegerProperty offset;
    
    public AdditionalParticipantPacket(ByteBuffer data) throws UnsupportedEncodingException {
        super(data);          
        
        this.offset = new SimpleIntegerProperty(ReadChar(data));
            
        super.setNames(data);
    }
    
    public Integer getOffset() {
        return offset.get();
    }
}