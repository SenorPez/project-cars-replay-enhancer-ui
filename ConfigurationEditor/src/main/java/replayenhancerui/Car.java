/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package replayenhancerui;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author SenorPez
 */
public class Car {
    final SimpleStringProperty carName;
    final SimpleObjectProperty<CarClass> carClass;
    
    public Car(String name) {
        this.carName = new SimpleStringProperty(name);
        this.carClass = null;
    }
    
    public Car(String name, CarClass carClass) {
        this.carName = new SimpleStringProperty(name);
        this.carClass = new SimpleObjectProperty<CarClass>(carClass);
    }
    
    public String getCarName() {
        return carName.get();
    }
    
    public CarClass getCarClass() {
        return carClass.get();
    }
    
    public void setCarName(String value) {
        carName.set(value);
    }
    
    public void setCarClass(CarClass value) {
        carClass.set(value);
    }
}
