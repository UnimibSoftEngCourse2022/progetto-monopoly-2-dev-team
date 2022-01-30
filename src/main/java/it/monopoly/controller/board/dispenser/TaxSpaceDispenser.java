package it.monopoly.controller.board.dispenser;

import it.monopoly.controller.board.space.Space;
import it.monopoly.controller.board.space.TaxSpace;
import it.monopoly.controller.command.CommandBuilderDispatcher;
import it.monopoly.manager.randomizer.RandomizationManager;

public class TaxSpaceDispenser extends SpaceDispenser {

    private final int[] taxes = {200, 100};
    private final RandomizationManager randomizationManager;
    private int index = 0;

    public TaxSpaceDispenser(CommandBuilderDispatcher commandBuilderDispatcher,
                             RandomizationManager randomizationManager,
                             int... spaceIndex) {
        super(commandBuilderDispatcher, spaceIndex);
        this.randomizationManager = randomizationManager;
    }

    @Override
    protected Space getSpaceInstance() {
        if (index < taxes.length) {
            return new TaxSpace(commandBuilderDispatcher, randomizationManager, taxes[index++]);
        }
        return null;
    }
}
