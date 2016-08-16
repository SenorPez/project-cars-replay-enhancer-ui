/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package replayenhancerui;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author senor
 */
public class PointStructureItemTest {
    /**
     * Test of getFinishPosition method, of class PointStructureItem.
     */
    @Test
    public void testGetFinishPosition() {
        System.out.println("getFinishPosition");
        PointStructureItem instance = new PointStructureItem(1, 25);
        Integer expResult = 1;
        Integer result = instance.getFinishPosition();
        assertEquals(expResult, result);
    }

    /**
     * Test of setPoints method, of class PointStructureItem.
     */
    @Test
    public void testSetPoints() {
        System.out.println("setPoints");
        Integer value = 25;
        PointStructureItem instance = new PointStructureItem(1, 15);
        instance.setPoints(value);
        assertEquals(value, instance.getPoints());
    }

    /**
     * Test of getPoints method, of class PointStructureItem.
     */
    @Test
    public void testGetPoints() {
        System.out.println("getPoints");
        PointStructureItem instance = new PointStructureItem(1, 25);
        Integer expResult = 25;
        Integer result = instance.getPoints();
        assertEquals(expResult, result);
    }

    /**
     * Test of setFinishPosition method, of class PointStructureItem.
     */
    @Test
    public void testSetFinishPosition() {
        System.out.println("setFinishPosition");
        Integer value = 1;
        PointStructureItem instance = new PointStructureItem(5, 25);
        instance.setFinishPosition(value);
        assertEquals(value, instance.getFinishPosition());
    }
    
}
