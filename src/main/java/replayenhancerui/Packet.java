/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package replayenhancerui;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

/**
 *
 * @author SenorPez
 */
public abstract class Packet {

    private final SimpleIntegerProperty buildVersionNumber;
    private final SimpleIntegerProperty packetType;    
    
    private SimpleSetProperty<SimpleStringProperty> names;
    
    protected Packet(ByteBuffer data) {
        this.buildVersionNumber = new SimpleIntegerProperty(ReadShort(data));
        this.packetType = new SimpleIntegerProperty(ReadChar(data));
        this.names = new SimpleSetProperty<SimpleStringProperty>(FXCollections.observableSet());
    }
     
    protected final Integer ReadShort(ByteBuffer data) {
        byte[] readBytes = new byte[2];
        data.get(readBytes);
        return ((0x00 << 24) + (0x00 << 16) + (readBytes[1] << 8) + (readBytes[0]));
    }
    
    protected final Integer ReadChar(ByteBuffer data) {
        byte[] readBytes = new byte[1];
        data.get(readBytes);
        return ((0x00 << 24) + (0x00 << 16) + (0x00 << 8) + (readBytes[0]));
    }
    
    protected final Float ReadFloat(ByteBuffer data) {
        byte[] readBytes = new byte[4];
        data.get(readBytes);
        return ByteBuffer.wrap(readBytes).getFloat();
    }
    
    protected final String ReadString(ByteBuffer data, Integer length) {
        byte[] readBytes = new byte[length];
        data.get(readBytes);
        try {
            return new String(readBytes, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Packet.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    
    public Integer getBuildVersionNumber() {
        return buildVersionNumber.get();
    }
    
    public Integer getPacketType() {
        return packetType.get();
    }

    public ObservableSet<SimpleStringProperty> getNames() {
        return names.get();
    }
    
    protected void setNames(ByteBuffer data) {
        for (int i=0; i<16; i++) {
            this.names.add(new SimpleStringProperty(ReadString(data, 64)));
        }
    }
}