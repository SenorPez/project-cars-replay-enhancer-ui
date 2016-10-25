/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurationeditor;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Car {
    private final SimpleStringProperty carName;
    private final SimpleObjectProperty<CarClass> carClass;

    @Override
    public int hashCode() {
        return this.getCarName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == this.getClass()) {
            Car newCar = (Car) obj;
            return this.getCarName().equals(newCar.getCarName());
        }
        return false;
    }

    public Car(String name) {
        this.carName = new SimpleStringProperty(name);
        this.carClass = new SimpleObjectProperty<>(null);
    }
    
    public Car(String name, CarClass carClass) {
        this.carName = new SimpleStringProperty(name);
        this.carClass = new SimpleObjectProperty<>(carClass);
    }

    public String getCarName() {
        return carName.get();
    }

    public SimpleStringProperty carNameProperty() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName.set(carName);
    }

    public CarClass getCarClass() {
        return carClass.get();
    }

    public SimpleObjectProperty<CarClass> carClassProperty() {
        return carClass;
    }

    public void setCarClass(CarClass carClass) {
        this.carClass.set(carClass);
    }
}
