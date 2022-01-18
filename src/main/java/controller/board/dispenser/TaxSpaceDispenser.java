package controller.board.dispenser;

import controller.board.space.Space;
import controller.board.space.TaxSpace;

public class TaxSpaceDispenser extends SpaceDispenser {

    public TaxSpaceDispenser(int... spaceIndex) {
        super(spaceIndex);
    }

    @Override
    protected Space getSpaceInstance() {
        return new TaxSpace();
    }
}
