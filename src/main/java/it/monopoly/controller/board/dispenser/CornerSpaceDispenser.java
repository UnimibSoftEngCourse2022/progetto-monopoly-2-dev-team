package it.monopoly.controller.board.dispenser;

import it.monopoly.controller.board.space.CornerSpace;
import it.monopoly.controller.board.space.Space;

public class CornerSpaceDispenser extends SpaceDispenser {

    public CornerSpaceDispenser(int... spaceIndex) {
        super(spaceIndex);
    }

    @Override
    protected Space getSpaceInstance() {
        return new CornerSpace();
    }
}
