/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package replayenhancerui;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

/**
 *
 * @author SenorPez
 */
public class ParticipantPacket extends Packet {
    public final SimpleStringProperty carName;
    public final SimpleStringProperty carClass;
    public final SimpleStringProperty trackLocation;
    public final SimpleStringProperty trackVariation;
        
    public SimpleSetProperty<SimpleFloatProperty> fastestLapTimes;
    
    public ParticipantPacket(ByteBuffer data) throws UnsupportedEncodingException {
        this.buildVersionNumber = new SimpleIntegerProperty(ReadShort(data));
        this.packetType = new SimpleIntegerProperty(ReadChar(data));
        
        this.carName = new SimpleStringProperty(ReadString(data, 64));
        this.carClass = new SimpleStringProperty(ReadString(data, 64));
        this.trackLocation = new SimpleStringProperty(ReadString(data, 64));
        this.trackVariation = new SimpleStringProperty(ReadString(data, 64));
        
        this.names = new SimpleSetProperty<SimpleStringProperty>(FXCollections.observableSet());
        this.fastestLapTimes = new SimpleSetProperty<SimpleFloatProperty>(FXCollections.observableSet());
        
        for (int i=0; i<16; i++) {
            this.names.add(new SimpleStringProperty(ReadString(data, 64)));
        }
        for (int i=0; i<16; i++) {
            this.fastestLapTimes.add(new SimpleFloatProperty(ReadFloat(data)));
        }
    }
    
    public String getCarName() {
        return carName.get();
    }
    
    public String getCarClass() {
        return carClass.get();
    }
    
    public String getTrackLocation() {
        return trackLocation.get();
    }
    
    public String getTrackVariation() {
        return trackVariation.get();
    }
    
    public ObservableSet getFastestLapTimes() {
        return fastestLapTimes.get();
    }
}
