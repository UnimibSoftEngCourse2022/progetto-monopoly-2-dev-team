package controller.board.dispenser;

import controller.board.space.ChanceSpace;
import controller.board.space.Space;

public class ChanceSpaceDispenser extends SpaceDispenser {

    public ChanceSpaceDispenser(int... spaceIndex) {
        super(spaceIndex);
    }

    @Override
    protected Space getSpaceInstance() {
        return new ChanceSpace();
    }
}
