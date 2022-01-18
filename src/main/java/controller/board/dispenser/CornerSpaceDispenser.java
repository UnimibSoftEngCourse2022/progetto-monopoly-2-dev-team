package controller.board.dispenser;

import controller.board.space.CornerSpace;
import controller.board.space.Space;
import controller.command.CommandBuilderDispatcher;

public class CornerSpaceDispenser extends SpaceDispenser {

    public CornerSpaceDispenser(CommandBuilderDispatcher commandBuilderDispatcher,
                                int... spaceIndex) {
        super(commandBuilderDispatcher, spaceIndex);
    }

    @Override
    protected Space getSpaceInstance() {
        return new CornerSpace(commandBuilderDispatcher);
    }
}
