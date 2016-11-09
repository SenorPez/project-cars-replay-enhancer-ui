/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurationeditor;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author SenorPez
 */
public class PacketTest {
//    private static File packet;

//    @BeforeClass
//    public static void setUpClass() {
//        packet = new File("src/test/resources/assets/race21/pdata4");
//    }
    
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
        ByteBuffer data = ByteBuffer.allocate(Float.BYTES).putFloat(Float.MAX_VALUE);
        data.rewind();
        Float expResult = new Float(Float.MAX_VALUE);
        Float result = Packet.ReadFloat(data);
        assertEquals(expResult, result);
    }

    /**
     * Test of ReadString method, of class Packet.
     */
    @Test
    public void testReadString() {
        System.out.println("ReadString");
        String testString = "Lotus 98T";
        ByteBuffer data = ByteBuffer.allocate(testString.length()).put(testString.getBytes());
        data.rewind();
        Integer length = testString.length();        
        String expResult = testString;
        String result = Packet.ReadString(data, length);
        assertEquals(expResult, result);
    }

    /**
     * Test of getBuildVersionNumber method, of class Packet.
     * @throws java.io.IOException
     */
//    @Test
//    public void testGetBuildVersionNumber() throws IOException {
//        System.out.println("getBuildVersionNumber");
//        Packet instance = new ParticipantPacket(
//            ByteBuffer.wrap(Files.readAllBytes(packet.toPath())));
//        Integer expResult = 38404;
//        Integer result = instance.getBuildVersionNumber();
//        assertEquals(expResult, result);
//    }

    /**
     * Test of getPacketType method, of class Packet.
     * @throws java.io.IOException
     */
//    @Test
//    public void testGetPacketType() throws IOException {
//        System.out.println("getPacketType");
//        Packet instance = new ParticipantPacket(
//            ByteBuffer.wrap(Files.readAllBytes(packet.toPath())));
//        Integer expResult = 49;
//        Integer result = instance.getPacketType();
//        assertEquals(expResult, result);
//    }

    /**
     * Test of getNames method, of class Packet.
     * @throws java.io.IOException
     */
//    @Test
//    public void testGetNames() throws IOException {
//        System.out.println("getNames");
//        Packet instance = new ParticipantPacket(
//            ByteBuffer.wrap(Files.readAllBytes(packet.toPath())));
//
//        String[] expResult = new String[16];
//        expResult[0] = "Darian May";
//        expResult[1] = "José Javier Buisán";
//        expResult[2] = "Kobernulf Monnur";
//        expResult[3] = "Steven Toth";
//        expResult[4] = "Murray Smith";
//        expResult[5] = "Adam Viljoen";
//        expResult[6] = "Forest Herve";
//        expResult[7] = "Matthew Mul";
//        expResult[8] = "Antti Sipola";
//        expResult[9] = "Thomas Einöder";
//        expResult[10] = "Arnaud Puiravaud";
//        expResult[11] = "Elliot Teague";
//        expResult[12] = "Mauricio Matos";
//        expResult[13] = "Nevil Wigbels";
//        expResult[14] = "Daniele Di Genni";
//        expResult[15] = "Iain Chalmers";
//
//        ObservableList<SimpleStringProperty> result = instance.getNames();
//        String[] resultValues = new String[16];
//        int i = 0;
//        for (SimpleStringProperty stringProp : result) {
//            resultValues[i++] = stringProp.getValue();
//        }
//
//        Arrays.sort(expResult);
//        Arrays.sort(resultValues);
//        assertArrayEquals(expResult, resultValues);
//    }

//    public class PacketImpl extends Packet {
//        public PacketImpl() {
//            super(null);
//        }
//    }
    
}
