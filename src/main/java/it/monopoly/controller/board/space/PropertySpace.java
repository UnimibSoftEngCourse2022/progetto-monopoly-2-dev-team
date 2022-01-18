package it.monopoly.controller.board.space;

import it.monopoly.model.property.PropertyModel;

public class PropertySpace implements Space{

    private PropertyModel property;

    public PropertySpace(PropertyModel property) {
        this.property = property;
    }

    public PropertyModel getProperty() {
        return property;
    }
}