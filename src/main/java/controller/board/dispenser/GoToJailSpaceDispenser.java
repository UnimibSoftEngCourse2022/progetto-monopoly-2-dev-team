package controller.board.dispenser;

import controller.board.space.GoToJailSpace;
import controller.board.space.Space;

public class GoToJailSpaceDispenser extends SpaceDispenser {

    public GoToJailSpaceDispenser(int... spaceIndex) {
        super(spaceIndex);
    }

    @Override
    protected Space getSpaceInstance() {
        return new GoToJailSpace();
    }
}
