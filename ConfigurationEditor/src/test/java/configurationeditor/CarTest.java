/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurationeditor;

import javafx.scene.paint.Color;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author senor
 */
public class CarTest {
    /**
     * Test of getCarName method, of class Car.
     */
    @Test
    public void testGetCarName() {
        System.out.println("getCarName");
        Car instance = new Car("Lotus 98T", new CarClass("", Color.RED));
        String expResult = "Lotus 98T";
        String result = instance.getCarName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCarClass method, of class Car.
     */
    @Test
    public void testGetCarClass() {
        System.out.println("getCarClass");
        Car instance = new Car("Lotus 98T", new CarClass("Historic Formula", Color.RED));
        CarClass expResult = new CarClass("Historic Formula", Color.RED);
        CarClass result = instance.getCarClass();
        assertTrue(
            result.getClassName().equals(expResult.getClassName())
            && result.getClassColor() == expResult.getClassColor());
    }

    /**
     * Test of setCarName method, of class Car.
     */
    @Test
    public void testSetCarName() {
        System.out.println("setCarName");
        String value = "Lotus 98T";
        Car instance = new Car("125cc Shifter Kart", new CarClass("", Color.RED));
        instance.setCarName(value);
        assertEquals(value, instance.getCarName());
    }

    /**
     * Test of setCarClass method, of class Car.
     */
    @Test
    public void testSetCarClass() {
        System.out.println("setCarClass");
        CarClass value = new CarClass("Historic Formula", Color.RED);
        Car instance = new Car("Lotus 98T", new CarClass("Kart 1", Color.BLUE));
        instance.setCarClass(value);
        assertEquals(value, instance.getCarClass());
    }
    
}
