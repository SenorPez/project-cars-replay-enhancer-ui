/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurationeditor;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author SenorPez
 */
public class PointStructureItem {
    final SimpleIntegerProperty points;
    final int finishPosition;

    @Override
    public int hashCode() {
        return finishPosition;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null
                && obj.getClass() == this.getClass()
                && this.finishPosition == ((PointStructureItem) obj).finishPosition;
    }

    public PointStructureItem(int finishPosition, Integer points) {
        this.points = new SimpleIntegerProperty(points);
        this.finishPosition = finishPosition;
    }

    public int getFinishPosition() {
        return finishPosition;
    }

    public void setPoints(Integer value) {
        points.set(value);
    }

    public Integer getPoints() {
        return points.get();
    }

    public IntegerProperty pointsProperty() {
        return points;
    }
}
