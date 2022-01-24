package it.monopoly.controller.board.dispenser;

import it.monopoly.controller.board.space.CornerSpace;
import it.monopoly.controller.board.space.Space;
import it.monopoly.controller.command.CommandBuilderDispatcher;
import it.monopoly.controller.board.space.CornerSpace;
import it.monopoly.controller.board.space.Space;

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
