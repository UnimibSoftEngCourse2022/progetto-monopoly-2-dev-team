package controller.board.dispenser;

import controller.board.space.CornerSpace;
import controller.board.space.Space;

public class CornerSpaceDispenser extends SpaceDispenser {

    public CornerSpaceDispenser(int... spaceIndex) {
        super(spaceIndex);
    }

    @Override
    protected Space getSpaceInstance() {
        return new CornerSpace();
    }
}
