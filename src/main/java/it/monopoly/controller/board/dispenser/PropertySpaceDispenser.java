package it.monopoly.controller.board.dispenser;

import it.monopoly.controller.board.space.PropertySpace;
import it.monopoly.controller.board.space.Space;
import it.monopoly.controller.command.CommandBuilderDispatcher;
import it.monopoly.model.property.PropertyModel;

import java.util.List;

public class PropertySpaceDispenser extends SpaceDispenser {

    private final List<PropertyModel> properties;
    private int index = 0;

    public PropertySpaceDispenser(CommandBuilderDispatcher commandBuilderDispatcher,
                                  List<PropertyModel> properties,
                                  int... spaceIndex) {
        super(commandBuilderDispatcher, spaceIndex);
        this.properties = properties;
    }

    @Override
    protected Space getSpaceInstance() {
        if (properties != null && index < properties.size()) {
            return new PropertySpace(commandBuilderDispatcher, properties.get(index++));
        }
        return null;
    }
}
