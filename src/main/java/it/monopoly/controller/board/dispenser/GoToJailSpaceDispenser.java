package it.monopoly.controller.board.dispenser;

import it.monopoly.controller.board.space.GoToJailSpace;
import it.monopoly.controller.board.space.Space;
import it.monopoly.controller.command.CommandBuilderDispatcher;
import it.monopoly.controller.player.PlayerController;

public class GoToJailSpaceDispenser extends SpaceDispenser {

    private PlayerController playerController;

    public GoToJailSpaceDispenser(CommandBuilderDispatcher commandBuilderDispatcher,
                                  PlayerController playerController,
                                  int... spaceIndex) {
        super(commandBuilderDispatcher, spaceIndex);
        this.playerController = playerController;
    }

    @Override
    protected Space getSpaceInstance() {
        return new GoToJailSpace(commandBuilderDispatcher, playerController);
    }
}
