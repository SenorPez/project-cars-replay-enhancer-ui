/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package replayenhancerui;

import java.nio.ByteBuffer;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

/**
 *
 * @author SenorPez
 */
public abstract class Packet {

    public SimpleIntegerProperty buildVersionNumber;
    public SimpleIntegerProperty packetType;    
    
    public ObservableList<SimpleStringProperty> names;

    protected final byte[] ReadBytes(ByteBuffer data, Integer size) {
        byte[] readBytes = new byte[size];
        data.get(readBytes);
        return readBytes;
    }
    
    public Integer getBuildVersionNumber() {
        return buildVersionNumber.get();
    }
    
    public Integer packetType() {
        return packetType.get();
    }

    public ObservableList getNames() {
        return names;
    }
}
