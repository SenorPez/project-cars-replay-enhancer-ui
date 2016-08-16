/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package replayenhancerui;

import javafx.scene.paint.Color;
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
public class CarClassTest {
    /**
     * Test of getClassName method, of class CarClass.
     */
    @Test
    public void testGetClassName() {
        System.out.println("getClassName");
        CarClass instance = new CarClass("Historic Formula", Color.RED);
        String expResult = "Historic Formula";
        String result = instance.getClassName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getClassColor method, of class CarClass.
     */
    @Test
    public void testGetClassColor() {
        System.out.println("getClassColor");
        CarClass instance = new CarClass("Historic Formula", Color.RED);
        Color expResult = Color.RED;
        Color result = instance.getClassColor();
        assertEquals(expResult, result);
    }

    /**
     * Test of setClassName method, of class CarClass.
     */
    @Test
    public void testSetClassName() {
        System.out.println("setClassName");
        String value = "Historic Formula";
        CarClass instance = new CarClass("Kart 1", Color.RED);
        instance.setClassName(value);
        assertEquals(value, instance.getClassName());
    }

    /**
     * Test of setClassColor method, of class CarClass.
     */
    @Test
    public void testSetClassColor() {
        System.out.println("setClassColor");
        Color value = Color.RED;
        CarClass instance = new CarClass("Historic Formula", Color.BLUE);
        instance.setClassColor(value);
        assertEquals(value, instance.getClassColor());
    }
    
}
