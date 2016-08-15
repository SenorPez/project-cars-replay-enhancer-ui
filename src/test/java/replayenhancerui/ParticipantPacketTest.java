/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package replayenhancerui;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.beans.property.SimpleFloatProperty;
import javafx.collections.ObservableList;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author 502625185
 */
public class ParticipantPacketTest {
    private static File packet;
    
    @BeforeClass
    public static void setUpClass() {
        packet = new File("src/test/resources/assets/race21/pdata4");
    }

    /**
     * Test of getCarName method, of class ParticipantPacket.
     * @throws java.io.IOException
     */
    @Test
    public void testGetCarName() throws IOException {
        System.out.println("getCarName");
        ParticipantPacket instance = new ParticipantPacket(
                ByteBuffer.wrap(Files.readAllBytes(packet.toPath())));
        String expResult = "Caterham Seven Classic";
        String result = instance.getCarName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCarClass method, of class ParticipantPacket.
     * @throws java.io.IOException
     */
    @Test
    public void testGetCarClass() throws IOException {
        System.out.println("getCarClass");
        ParticipantPacket instance = new ParticipantPacket(
                ByteBuffer.wrap(Files.readAllBytes(packet.toPath())));
        String expResult = "Road D";
        String result = instance.getCarClass();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTrackLocation method, of class ParticipantPacket.
     * @throws java.io.IOException
     */
    @Test
    public void testGetTrackLocation() throws IOException {
        System.out.println("getTrackLocation");
        ParticipantPacket instance = new ParticipantPacket(
                ByteBuffer.wrap(Files.readAllBytes(packet.toPath())));
        String expResult = "Oulton Park";
        String result = instance.getTrackLocation();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTrackVariation method, of class ParticipantPacket.
     * @throws java.io.IOException
     */
    @Test
    public void testGetTrackVariation() throws IOException {
        System.out.println("getTrackVariation");
        ParticipantPacket instance = new ParticipantPacket(
                ByteBuffer.wrap(Files.readAllBytes(packet.toPath())));
        String expResult = "Fosters";
        String result = instance.getTrackVariation();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFastestLapTimes method, of class ParticipantPacket.
     * @throws java.io.IOException
     */
    @Test
    public void testGetFastestLapTimes() throws IOException {
        System.out.println("getFastestLapTimes");
        ParticipantPacket instance = new ParticipantPacket(
                ByteBuffer.wrap(Files.readAllBytes(packet.toPath())));

        Float[] expResult = new Float[16];
        Arrays.setAll(expResult, i -> new Float(0));
        
        ObservableList<SimpleFloatProperty> result = instance.getFastestLapTimes();
        List<Float> resultValues = new ArrayList<>();
        for (SimpleFloatProperty floatProp: result) {
            resultValues.add(floatProp.getValue());
        }
        assertArrayEquals(expResult, resultValues.toArray());
    }
    
}
