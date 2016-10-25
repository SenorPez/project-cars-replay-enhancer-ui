/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurationeditor;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Driver {
    private final SimpleStringProperty team;
    private final SimpleStringProperty shortName;
    private final SimpleStringProperty displayName;
    private final SimpleStringProperty name;
    private final SimpleObjectProperty<Car> car;
    private final SimpleIntegerProperty seriesPoints;

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

    public Driver(String name, String displayName, String shortName, Car car, String team, Integer points) {
        this.name = new SimpleStringProperty(name);
        this.displayName = new SimpleStringProperty(displayName);
        this.shortName = new SimpleStringProperty(shortName);
        this.car = new SimpleObjectProperty<>(car);
        this.team = new SimpleStringProperty(team);
        this.seriesPoints = new SimpleIntegerProperty(points);
    }

    public String getTeam() {
        return team.get();
    }

    public SimpleStringProperty teamProperty() {
        return team;
    }

    public void setTeam(String team) {
        this.team.set(team);
    }

    public String getShortName() {
        return shortName.get();
    }

    public SimpleStringProperty shortNameProperty() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName.set(shortName);
    }

    public String getDisplayName() {
        return displayName.get();
    }

    public SimpleStringProperty displayNameProperty() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName.set(displayName);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public Car getCar() {
        return car.get();
    }

    public SimpleObjectProperty<Car> carProperty() {
        return car;
    }

    public void setCar(Car car) {
        this.car.set(car);
    }

    public int getSeriesPoints() {
        return seriesPoints.get();
    }

    public SimpleIntegerProperty seriesPointsProperty() {
        return seriesPoints;
    }

    public void setSeriesPoints(int seriesPoints) {
        this.seriesPoints.set(seriesPoints);
    }
}
