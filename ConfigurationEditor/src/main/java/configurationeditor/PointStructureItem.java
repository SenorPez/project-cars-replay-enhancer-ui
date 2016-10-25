/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurationeditor;

import javafx.beans.property.SimpleIntegerProperty;

public class PointStructureItem {
    final SimpleIntegerProperty points;
    final SimpleIntegerProperty finishPosition;

    public PointStructureItem(int finishPosition, Integer points) {
        this.finishPosition = new SimpleIntegerProperty(finishPosition);
        this.points = new SimpleIntegerProperty(points);
    }

    public Integer getFinishPosition() {
        return finishPosition.get();
    }

    public void setFinishPosition(Integer value) {
        finishPosition.set(value);
    }

    public Integer getPoints() {
        return points.get();
    }

    public void setPoints(Integer value) {
        points.set(value);
    }
}
