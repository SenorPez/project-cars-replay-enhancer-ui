/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package replayenhancerui;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

/**
 *
 * @author SenorPez
 */
public class ParticipantPacket extends Packet {
    private final SimpleStringProperty carName;
    private final SimpleStringProperty carClass;
    private final SimpleStringProperty trackLocation;
    private final SimpleStringProperty trackVariation;
        
    private final SimpleSetProperty<SimpleFloatProperty> fastestLapTimes;
    
    public ParticipantPacket(ByteBuffer data) throws UnsupportedEncodingException {
        super(data);
      
        this.carName = new SimpleStringProperty(ReadString(data, 64));
        this.carClass = new SimpleStringProperty(ReadString(data, 64));
        this.trackLocation = new SimpleStringProperty(ReadString(data, 64));
        this.trackVariation = new SimpleStringProperty(ReadString(data, 64));
        
        this.fastestLapTimes = new SimpleSetProperty<SimpleFloatProperty>(FXCollections.observableSet());
        
        super.setNames(data);
        
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
    
    public ObservableSet<SimpleFloatProperty> getFastestLapTimes() {
        return fastestLapTimes.get();
    }
}
