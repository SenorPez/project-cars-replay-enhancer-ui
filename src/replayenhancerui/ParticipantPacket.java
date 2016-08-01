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
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

/**
 *
 * @author SenorPez
 */
public class ParticipantPacket extends Packet {
    public final SimpleStringProperty carName;
    public final SimpleStringProperty carClass;
    public final SimpleStringProperty trackLocation;
    public final SimpleStringProperty trackVariation;
        
    public final ObservableList<SimpleFloatProperty> fastestLapTimes;
    
    public ParticipantPacket(ByteBuffer data) throws UnsupportedEncodingException {
            this.buildVersionNumber = new SimpleIntegerProperty(ByteBuffer.wrap(ReadBytes(data, 2)).getInt());
            this.packetType = new SimpleIntegerProperty(ByteBuffer.wrap(ReadBytes(data, 1)).getInt());
            this.carName = new SimpleStringProperty(new String(ReadBytes(data, 64), "UTF-8"));
            this.carClass = new SimpleStringProperty(new String(ReadBytes(data, 64), "UTF-8"));
            this.trackLocation = new SimpleStringProperty(new String(ReadBytes(data, 64), "UTF-8"));
            this.trackVariation = new SimpleStringProperty(new String(ReadBytes(data, 64), "UTF-8"));
            this.names = null;
            this.fastestLapTimes = null;
            for (int i=0; i<16; i++) {
                this.names.add(new SimpleStringProperty(new String(ReadBytes(data, 64), "UTF-8")));
            }
            for (int i=0; i<16; i++) {
                this.fastestLapTimes.add(new SimpleFloatProperty(ByteBuffer.wrap(ReadBytes(data, 4)).getFloat()));
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
}
