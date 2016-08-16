/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurationeditor;


import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.paint.Color;

/**
 *
 * @author SenorPez
 */
public class CarClass {
    final SimpleStringProperty className;
    final SimpleObjectProperty<Color> classColor;
    
    public CarClass(String className, Color classColor) {
        this.className = new SimpleStringProperty(className);
        this.classColor = new SimpleObjectProperty<Color>(classColor);
    }
    
    public String getClassName() {
        return className.get();
    }
    
    public Color getClassColor() {
        return classColor.get();
    }
    
    public void setClassName(String value) {
        className.set(value);
    }
    
    public void setClassColor(Color value) {
        classColor.set(value);
    }
}
