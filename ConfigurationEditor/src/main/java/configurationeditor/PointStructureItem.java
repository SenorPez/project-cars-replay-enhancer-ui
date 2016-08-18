/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurationeditor;

import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author SenorPez
 */
public class PointStructureItem {
    final SimpleIntegerProperty points;
    final SimpleIntegerProperty finishPosition;

    @Override
    public int hashCode() {
        return this.getFinishPosition().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == this.getClass()) {
            PointStructureItem thisObj = (PointStructureItem) obj;
            return thisObj.getFinishPosition() == this.getFinishPosition();
        }
        return false;
    }

    public PointStructureItem(Integer finishPosition, Integer points) {
        this.points = new SimpleIntegerProperty(points);
        this.finishPosition = new SimpleIntegerProperty(finishPosition);
    }

    public Integer getFinishPosition() {
        return finishPosition.get();
    }

    public void setPoints(Integer value) {
        points.set(value);
    }

    public Integer getPoints() {
        return points.get();
    }

    public void setFinishPosition(Integer value) {
        finishPosition.set(value);
    }
}
