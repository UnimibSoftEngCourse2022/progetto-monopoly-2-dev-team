package it.monopoly.controller.board.dispenser;

import it.monopoly.controller.board.space.CommunityChestSpace;
import it.monopoly.controller.board.space.Space;

public class CommunityChestSpaceDispenser extends SpaceDispenser {

    public CommunityChestSpaceDispenser(int... spaceIndex) {
        super(spaceIndex);
    }

    @Override
    protected Space getSpaceInstance() {
        return new CommunityChestSpace();
    }
}
