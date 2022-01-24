package it.monopoly.controller.board.dispenser;

import it.monopoly.controller.board.space.Space;
import it.monopoly.controller.board.space.TaxSpace;
import it.monopoly.controller.board.space.Space;
import it.monopoly.controller.board.space.TaxSpace;
import it.monopoly.controller.command.CommandBuilderDispatcher;

public class TaxSpaceDispenser extends SpaceDispenser {

    private final int[] taxes = {200, 100};
    private int index = 0;

    public TaxSpaceDispenser(CommandBuilderDispatcher commandBuilderDispatcher,
                             int... spaceIndex) {
        super(commandBuilderDispatcher, spaceIndex);
    }

    @Override
    protected Space getSpaceInstance() {
        if (index < taxes.length) {
            return new TaxSpace(commandBuilderDispatcher, taxes[index++]);
        }
        return null;
    }
}
