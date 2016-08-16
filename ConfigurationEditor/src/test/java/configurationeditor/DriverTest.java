/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurationeditor;

import configurationeditor.Driver;
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
public class DriverTest {
    
    public DriverTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getCar method, of class Driver.
     */
    @Test
    public void testGetCar() {
        System.out.println("getCar");
        Driver instance = new Driver("Kobernulf Monnur");
        String expResult = "Lotus 98T";
        instance.setCar(expResult);
        String result = instance.getCar();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTeam method, of class Driver.
     */
    @Test
    public void testGetTeam() {
        System.out.println("getTeam");
        Driver instance = new Driver("Kobernulf Monnur");
        String expResult = "DarkNitro";
        instance.setTeam(expResult);
        String result = instance.getTeam();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSeriesPoints method, of class Driver.
     */
    @Test
    public void testGetSeriesPoints() {
        System.out.println("getSeriesPoints");
        Driver instance = new Driver("Kobernulf Monnur");
        Integer expResult = 50;
        instance.setSeriesPoints(expResult);
        Integer result = instance.getSeriesPoints();
        assertEquals(expResult, result);
    }

    /**
     * Test of setSeriesPoints method, of class Driver.
     */
    @Test
    public void testSetSeriesPoints() {
        System.out.println("setSeriesPoints");
        Integer value = 50;
        Driver instance = new Driver("Kobernulf Monnur");
        instance.setSeriesPoints(value);
        assertEquals(value, instance.getSeriesPoints());
    }

    /**
     * Test of setName method, of class Driver.
     */
    @Test
    public void testSetName() {
        System.out.println("setName");
        String value = "Kobernulf Monnur";
        Driver instance = new Driver("Aryton Senna");
        instance.setName(value);
        assertEquals(value, instance.getName());
    }

    /**
     * Test of setCar method, of class Driver.
     */
    @Test
    public void testSetCar() {
        System.out.println("setCar");
        String value = "Lotus 98T";
        Driver instance = new Driver("Kobernulf Monnur");
        instance.setCar(value);
        assertEquals(value, instance.getCar());
    }

    /**
     * Test of getName method, of class Driver.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        Driver instance = new Driver("Kobernulf Monnur");
        String expResult = "Kobernulf Monnur";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setTeam method, of class Driver.
     */
    @Test
    public void testSetTeam() {
        System.out.println("setTeam");
        String value = "DarkNitro";
        Driver instance = new Driver("Kobernulf Monnur");
        instance.setTeam(value);
        assertEquals(value, instance.getTeam());
    }

    /**
     * Test of setDisplayName method, of class Driver.
     */
    @Test
    public void testSetDisplayName() {
        System.out.println("setDisplayName");
        String value = "Kobernulf Monnur";
        Driver instance = new Driver("Kobernulf Monnurdasdfer");
        instance.setDisplayName(value);
        assertEquals(value, instance.getDisplayName());
    }

    /**
     * Test of setShortName method, of class Driver.
     */
    @Test
    public void testSetShortName() {
        System.out.println("setShortName");
        String value = "K. Monnur";
        Driver instance = new Driver("Kobernulf Monnur");
        instance.setShortName(value);
        assertEquals(value, instance.getShortName());
    }

    /**
     * Test of getShortName method, of class Driver.
     */
    @Test
    public void testGetShortName() {
        System.out.println("getShortName");
        Driver instance = new Driver("Kobernulf Monnur");
        String expResult = "K. Monnur";
        instance.setShortName(expResult);
        String result = instance.getShortName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDisplayName method, of class Driver.
     */
    @Test
    public void testGetDisplayName() {
        System.out.println("getDisplayName");
        Driver instance = new Driver("Kobernulf Monnurasdf");
        String expResult = "Kobernulf Monnur";
        instance.setDisplayName(expResult);
        String result = instance.getDisplayName();
        assertEquals(expResult, result);
    }
    
}
