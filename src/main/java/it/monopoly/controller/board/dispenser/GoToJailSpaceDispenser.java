package it.monopoly.controller.board.dispenser;

import it.monopoly.controller.board.space.GoToJailSpace;
import it.monopoly.controller.board.space.Space;

public class GoToJailSpaceDispenser extends SpaceDispenser {

    public GoToJailSpaceDispenser(int... spaceIndex) {
        super(spaceIndex);
    }

    @Override
    protected Space getSpaceInstance() {
        return new GoToJailSpace();
    }
}
