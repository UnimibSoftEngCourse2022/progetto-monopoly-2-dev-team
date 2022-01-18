package controller.board.dispenser;

import controller.board.space.PropertySpace;
import controller.board.space.Space;
import model.property.PropertyModel;

import java.util.List;

public class PropertySpaceDispenser extends SpaceDispenser {

    private List<PropertyModel> properties;
    private int index = 0;

    public PropertySpaceDispenser(List<PropertyModel> properties, int... spaceIndex) {
        super(spaceIndex);
        this.properties = properties;
    }

    @Override
    protected Space getSpaceInstance() {
        if (properties != null && index < properties.size()) {
            return new PropertySpace(properties.get(index++));
        }
        return null;
    }
}
