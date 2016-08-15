/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package replayenhancerui;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableSet;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author 502625185
 */
public class PacketTest {
    /**
     * Test of ReadShort method, of class Packet.
     */
    @Test
    public void testReadShort() {
        System.out.println("ReadShort");
        ByteBuffer data = ByteBuffer.allocate(Short.BYTES).putShort(Short.MAX_VALUE);
        data.rewind();
        Integer expResult = new Integer(Short.MAX_VALUE);
        Integer result = Packet.ReadShort(data);
        assertEquals(expResult, result);
    }

    /**
     * Test of ReadChar method, of class Packet.
     */
    @Test
    public void testReadChar() {
        System.out.println("ReadChar");
        ByteBuffer data = ByteBuffer.allocate(Byte.BYTES).put(Byte.MAX_VALUE);
        data.rewind();
        Integer expResult = new Integer(Byte.MAX_VALUE);
        Integer result = Packet.ReadChar(data);
        assertEquals(expResult, result);
    }

    /**
     * Test of ReadChar method, of class Packet.
     */
    @Test
    public void testReadByte() {
        System.out.println("ReadByte");
        ByteBuffer data = ByteBuffer.allocate(Byte.BYTES).put(Byte.MAX_VALUE);
        data.rewind();
        Integer expResult = new Integer(Byte.MAX_VALUE);
        Integer result = Packet.ReadByte(data);
        assertEquals(expResult, result);
    }

    /**
     * Test of ReadFloat method, of class Packet.
     */
    @Test
    public void testReadFloat() {
        System.out.println("ReadFloat");
        ByteBuffer data = ByteBuffer.allocate(Float.BYTES).put((byte)Float.MAX_VALUE);
        data.rewind();
        Float expResult = Float.MAX_VALUE;
        Float result = Packet.ReadFloat(data);
        assertEquals(expResult, result);
    }

    /**
     * Test of ReadString method, of class Packet.
     */
    @Test
    public void testReadString() {
        System.out.println("ReadString");
        String testString = "I'm driving what I think is a real dragon.";
        ByteBuffer data = ByteBuffer.allocate(testString.length()).put(testString.getBytes());
        data.rewind();
        Integer length = testString.length();        
        String expResult = testString;
        String result = Packet.ReadString(data, length);
        assertEquals(expResult, result);
    }

    /**
     * Test of getBuildVersionNumber method, of class Packet.
     */
    @Test
    public void testGetBuildVersionNumber() {
        System.out.println("getBuildVersionNumber");
        Packet instance = null;
        Integer expResult = null;
        Integer result = instance.getBuildVersionNumber();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPacketType method, of class Packet.
     */
    @Test
    public void testGetPacketType() {
        System.out.println("getPacketType");
        Packet instance = null;
        Integer expResult = null;
        Integer result = instance.getPacketType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNames method, of class Packet.
     */
    @Test
    public void testGetNames() {
        System.out.println("getNames");
        Packet instance = null;
        ObservableSet<SimpleStringProperty> expResult = null;
        ObservableSet<SimpleStringProperty> result = instance.getNames();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setNames method, of class Packet.
     */
    @Test
    public void testSetNames() {
        System.out.println("setNames");
        ByteBuffer data = null;
        Packet instance = null;
        instance.setNames(data);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class PacketImpl extends Packet {

        public PacketImpl() {
            super(null);
        }
    }
    
}
