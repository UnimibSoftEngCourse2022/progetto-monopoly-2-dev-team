package it.monopoly.controller.board.dispenser;

import it.monopoly.controller.board.space.GoToJailSpace;
import it.monopoly.controller.board.space.Space;
import it.monopoly.controller.command.CommandBuilderDispatcher;
import it.monopoly.controller.player.PlayerController;

public class GoToJailSpaceDispenser extends SpaceDispenser {


    public GoToJailSpaceDispenser(CommandBuilderDispatcher commandBuilderDispatcher,
                                  int... spaceIndex) {
        super(commandBuilderDispatcher, spaceIndex);
    }

    @Override
    protected Space getSpaceInstance() {
        return new GoToJailSpace(commandBuilderDispatcher);
    }
}
