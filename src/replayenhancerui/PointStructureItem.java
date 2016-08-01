/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package replayenhancerui;

import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author SenorPez
 */
public class PointStructureItem {
    final SimpleIntegerProperty points;
    final SimpleIntegerProperty finishPosition;
    
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
