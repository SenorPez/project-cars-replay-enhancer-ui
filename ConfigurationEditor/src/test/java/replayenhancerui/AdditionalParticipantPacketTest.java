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
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author SenorPez
 */
public class AdditionalParticipantPacketTest {
    private static File packet;
    
    @BeforeClass
    public static void setUpClass() {
        packet = new File("src/test/resources/assets/race21/pdata9");
    }

    /**
     * Test of getOffset method, of class AdditionalParticipantPacket.
     * @throws java.io.IOException
     */
    @Test
    public void testGetOffset() throws IOException {
        System.out.println("getOffset");
        AdditionalParticipantPacket instance = new AdditionalParticipantPacket(
            ByteBuffer.wrap(Files.readAllBytes(packet.toPath())));
        Integer expResult = 16;
        Integer result = instance.getOffset();
        assertEquals(expResult, result);
    }
}
