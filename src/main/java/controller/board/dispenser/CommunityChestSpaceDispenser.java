package controller.board.dispenser;

import controller.board.space.CommunityChestSpace;
import controller.board.space.Space;

public class CommunityChestSpaceDispenser extends SpaceDispenser {

    public CommunityChestSpaceDispenser(int... spaceIndex) {
        super(spaceIndex);
    }

    @Override
    protected Space getSpaceInstance() {
        return new CommunityChestSpace();
    }
}
