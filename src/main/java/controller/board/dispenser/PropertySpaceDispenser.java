package controller.board.dispenser;

import controller.board.space.PropertySpace;
import controller.board.space.Space;

public class PropertySpaceDispenser extends SpaceDispenser {

    public PropertySpaceDispenser(int... spaceIndex) {
        super(spaceIndex);
    }

    @Override
    protected Space getSpaceInstance() {
        return new PropertySpace();
    }
}
