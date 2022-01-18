package controller.board.dispenser;

import controller.board.space.PropertySpace;
import controller.board.space.Space;
import controller.board.space.TaxSpace;
import controller.command.CommandBuilderDispatcher;

import java.util.List;

public class TaxSpaceDispenser extends SpaceDispenser {

    private final int[] taxes = {200, 100};
    private int index = 0;

    public TaxSpaceDispenser(CommandBuilderDispatcher commandBuilderDispatcher,
                            int... spaceIndex) {
        super(commandBuilderDispatcher, spaceIndex);
    }

    @Override
    protected Space getSpaceInstance() {
        if (taxes != null && index < taxes.length) {
            return new TaxSpace(commandBuilderDispatcher, taxes[index++]);
        }
        return null;
    }
}
