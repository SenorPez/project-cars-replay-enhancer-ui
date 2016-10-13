/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurationeditor;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author SenorPez
 */
public class Driver {
    final SimpleStringProperty team;
    final SimpleStringProperty shortName;
    final SimpleStringProperty displayName;
    final SimpleStringProperty name;
    final SimpleObjectProperty<Car> car;
    final SimpleIntegerProperty seriesPoints;

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == this.getClass()) {
            Driver driverObj = (Driver) obj;
            return this.getName().equals(driverObj.getName());
        }
        return false;
    }

    public Driver(String name) {
        this.name = new SimpleStringProperty(name);
        this.displayName = new SimpleStringProperty(name);
        this.shortName = new SimpleStringProperty(name);
        this.car = new SimpleObjectProperty<>(null);
        this.team = new SimpleStringProperty(null);
        this.seriesPoints = new SimpleIntegerProperty(0);
    }

    public Driver(String name, String displayName, String shortName, Car car) {
        this.name = new SimpleStringProperty(name);
        this.displayName = new SimpleStringProperty(displayName);
        this.shortName = new SimpleStringProperty(shortName);
        this.car = new SimpleObjectProperty<>(car);
        this.team = new SimpleStringProperty(null);
        this.seriesPoints = new SimpleIntegerProperty(0);
    }

    public Car getCar() {
        return car.get();
    }

    public String getCarName() {
        return car.get().getCarName();
    }

    public String getTeam() {
        return team.get();
    }

    public Integer getSeriesPoints() {
        return seriesPoints.get();
    }

    public void setSeriesPoints(Integer value) {
        seriesPoints.set(value);
    }

    public void setName(String value) {
        name.set(value);
    }

    public void setCar(Car value) {
        car.set(value);
    }

    public String getName() {
        return name.get();
    }

    public void setTeam(String value) {
        team.set(value);
    }

    public void setDisplayName(String value) {
        displayName.set(value);
    }

    public void setShortName(String value) {
        shortName.set(value);
    }

    public String getShortName() {
        return shortName.get();
    }

    public String getDisplayName() {
        return displayName.get();
    }
}
