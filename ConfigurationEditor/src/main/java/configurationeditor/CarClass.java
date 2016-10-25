/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurationeditor;


import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.paint.Color;

public class CarClass {
    private final SimpleStringProperty className;
    private final SimpleObjectProperty<Color> classColor;

    @Override
    public int hashCode() {
        return this.getClassName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == this.getClass()) {
            CarClass newCarClass = (CarClass) obj;
            return newCarClass.getClassName().equals(this.getClassName());
        }
        return false;
    }

    public CarClass(String className, Color classColor) {
        this.className = new SimpleStringProperty(className);
        this.classColor = new SimpleObjectProperty<>(classColor);
    }

    public String getClassName() {
        return className.get();
    }

    public SimpleStringProperty classNameProperty() {
        return className;
    }

    public void setClassName(String className) {
        this.className.set(className);
    }

    public Color getClassColor() {
        return classColor.get();
    }

    public SimpleObjectProperty<Color> classColorProperty() {
        return classColor;
    }

    public void setClassColor(Color classColor) {
        this.classColor.set(classColor);
    }
}
