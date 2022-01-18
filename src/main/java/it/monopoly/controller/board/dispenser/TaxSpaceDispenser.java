package it.monopoly.controller.board.dispenser;

import it.monopoly.controller.board.space.Space;
import it.monopoly.controller.board.space.TaxSpace;

public class TaxSpaceDispenser extends SpaceDispenser {

    public TaxSpaceDispenser(int... spaceIndex) {
        super(spaceIndex);
    }

    @Override
    protected Space getSpaceInstance() {
        return new TaxSpace();
    }
}
