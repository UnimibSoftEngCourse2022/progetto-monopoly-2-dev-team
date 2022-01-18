package controller.board.dispenser;

import controller.board.space.PropertySpace;
import controller.board.space.Space;
import controller.command.CommandBuilderDispatcher;
import model.property.PropertyModel;

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
