/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package replayenhancerui;

import javafx.beans.property.SimpleIntegerProperty;
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
    final SimpleStringProperty car;
    final SimpleIntegerProperty seriesPoints;
    
    public Driver(String name) {
        this.name = new SimpleStringProperty(name);
        this.displayName = new SimpleStringProperty(name);
        this.shortName = new SimpleStringProperty(name);
        this.car = new SimpleStringProperty("");
        this.team = new SimpleStringProperty("");
        this.seriesPoints = new SimpleIntegerProperty(0);
    }
    
    public Driver(String name, String displayName, String shortName, String car) {
        this.name = new SimpleStringProperty(name);
        this.displayName = new SimpleStringProperty(displayName);
        this.shortName = new SimpleStringProperty(shortName);
        this.car = new SimpleStringProperty(car);
        this.team = new SimpleStringProperty("");
        this.seriesPoints = new SimpleIntegerProperty(0);
    }

    public String getCar() {
        return car.get();
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

    public void setCar(String value) {
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
