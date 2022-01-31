package it.monopoly.controller.board.dispenser;

import it.monopoly.controller.board.space.JailSpace;
import it.monopoly.controller.board.space.Space;
import it.monopoly.controller.command.CommandBuilderDispatcher;
import it.monopoly.controller.event.EventDispatcher;
import it.monopoly.controller.player.PlayerController;

public class JailSpaceDispenser extends SpaceDispenser {

    private final PlayerController playerController;
    private final EventDispatcher eventDispatcher;

    public JailSpaceDispenser(CommandBuilderDispatcher commandBuilderDispatcher,
                              EventDispatcher eventDispatcher,
                              PlayerController playerController,
                              int... spaceIndex) {
        super(commandBuilderDispatcher, spaceIndex);
        this.playerController = playerController;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    protected Space getSpaceInstance() {
        return new JailSpace(playerController, commandBuilderDispatcher, eventDispatcher);
    }
}
