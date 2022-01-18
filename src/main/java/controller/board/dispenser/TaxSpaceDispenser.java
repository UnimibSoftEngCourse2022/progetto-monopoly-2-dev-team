package controller.board.dispenser;

import controller.board.space.Space;
import controller.board.space.TaxSpace;
import controller.command.CommandBuilderDispatcher;

public class TaxSpaceDispenser extends SpaceDispenser {

    public TaxSpaceDispenser(CommandBuilderDispatcher commandBuilderDispatcher,
                             int... spaceIndex) {
        super(commandBuilderDispatcher, spaceIndex);
    }

    @Override
    protected Space getSpaceInstance() {
        return new TaxSpace(commandBuilderDispatcher);
    }
}
