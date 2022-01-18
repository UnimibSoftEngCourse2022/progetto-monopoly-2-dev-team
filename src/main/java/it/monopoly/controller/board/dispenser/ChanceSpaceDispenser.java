package it.monopoly.controller.board.dispenser;

import it.monopoly.controller.board.space.ChanceSpace;
import it.monopoly.controller.board.space.Space;

public class ChanceSpaceDispenser extends SpaceDispenser {

    public ChanceSpaceDispenser(int... spaceIndex) {
        super(spaceIndex);
    }

    @Override
    protected Space getSpaceInstance() {
        return new ChanceSpace();
    }
}
