package configurationeditor;

import javafx.beans.property.SimpleIntegerProperty;

import java.nio.ByteBuffer;

public class AdditionalParticipantPacket extends Packet {
    private final SimpleIntegerProperty offset;
    
    public AdditionalParticipantPacket(ByteBuffer data) {
        super(data);          
        
        this.offset = new SimpleIntegerProperty(ReadChar(data));
            
        super.setNames(data);
    }
    
    public Integer getOffset() {
        return offset.get();
    }
}