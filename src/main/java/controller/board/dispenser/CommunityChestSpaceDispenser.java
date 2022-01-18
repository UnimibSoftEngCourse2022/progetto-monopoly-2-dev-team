package controller.board.dispenser;

import controller.board.space.CommunityChestSpace;
import controller.board.space.Space;
import controller.command.CommandBuilderDispatcher;

public class CommunityChestSpaceDispenser extends SpaceDispenser {

    public CommunityChestSpaceDispenser(CommandBuilderDispatcher commandBuilderDispatcher,
                                        int... spaceIndex) {
        super(commandBuilderDispatcher, spaceIndex);
    }

    @Override
    protected Space getSpaceInstance() {
        return new CommunityChestSpace(commandBuilderDispatcher);
    }
}
