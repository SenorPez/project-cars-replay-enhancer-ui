/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurationeditor;

import javafx.beans.property.SimpleIntegerProperty;

public class PointStructureItem {
    private final SimpleIntegerProperty points;
    private final SimpleIntegerProperty finishPosition;

    public PointStructureItem(int finishPosition, Integer points) {
        this.finishPosition = new SimpleIntegerProperty(finishPosition);
        this.points = new SimpleIntegerProperty(points);
    }

    public int getPoints() {
        return points.get();
    }

    public SimpleIntegerProperty pointsProperty() {
        return points;
    }

    public void setPoints(int points) {
        this.points.set(points);
    }

    public int getFinishPosition() {
        return finishPosition.get();
    }

    public SimpleIntegerProperty finishPositionProperty() {
        return finishPosition;
    }

    public void setFinishPosition(int finishPosition) {
        this.finishPosition.set(finishPosition);
    }
}
