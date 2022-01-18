package controller.board.dispenser;

import controller.board.space.ChanceSpace;
import controller.board.space.Space;
import controller.command.CommandBuilderDispatcher;

public class ChanceSpaceDispenser extends SpaceDispenser {

    public ChanceSpaceDispenser(CommandBuilderDispatcher commandBuilderDispatcher,
                                int... spaceIndex) {
        super(commandBuilderDispatcher, spaceIndex);
    }

    @Override
    protected Space getSpaceInstance() {
        return new ChanceSpace(commandBuilderDispatcher);
    }
}
