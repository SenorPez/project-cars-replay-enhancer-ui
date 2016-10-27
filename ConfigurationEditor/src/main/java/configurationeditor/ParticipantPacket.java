/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurationeditor;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ParticipantPacket extends Packet {
    private final SimpleStringProperty carName;
    private final SimpleStringProperty carClass;
    private final SimpleStringProperty trackLocation;
    private final SimpleStringProperty trackVariation;
        
    private final SimpleListProperty<SimpleFloatProperty> fastestLapTimes;
    
    public ParticipantPacket(ByteBuffer data) {
        super(data);
      
        this.carName = new SimpleStringProperty(ReadString(data, 64).trim());
        this.carClass = new SimpleStringProperty(ReadString(data, 64).trim());
        this.trackLocation = new SimpleStringProperty(ReadString(data, 64).trim());
        this.trackVariation = new SimpleStringProperty(ReadString(data, 64).trim());
        
        this.fastestLapTimes = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));
        
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
    
    public ObservableList<SimpleFloatProperty> getFastestLapTimes() {
        return fastestLapTimes.get();
    }
}
