package it.monopoly.controller.board.dispenser;

import it.monopoly.controller.board.space.PropertySpace;
import it.monopoly.controller.board.space.Space;
import it.monopoly.controller.command.CommandBuilderDispatcher;
import it.monopoly.controller.property.PropertyController;
import it.monopoly.model.property.PropertyModel;

import java.util.List;

public class PropertySpaceDispenser extends SpaceDispenser {

    private final List<PropertyModel> properties;
    private final PropertyController propertyController;
    private int index = 0;

    public PropertySpaceDispenser(CommandBuilderDispatcher commandBuilderDispatcher,
                                  PropertyController propertyController,
                                  int...spaceIndex) {
        super(commandBuilderDispatcher, spaceIndex);
        this.propertyController = propertyController;
        this.properties = propertyController.getModels();
    }

    @Override
    protected Space getSpaceInstance() {
        if (properties != null && index < properties.size()) {
            return new PropertySpace(commandBuilderDispatcher, propertyController.getManager(properties.get(index++)));
        }
        return null;
    }
}
