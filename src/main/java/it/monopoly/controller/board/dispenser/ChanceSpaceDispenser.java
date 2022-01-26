package it.monopoly.controller.board.dispenser;

import it.monopoly.controller.board.card.DrawableCardController;
import it.monopoly.controller.board.space.ChanceSpace;
import it.monopoly.controller.board.space.Space;
import it.monopoly.controller.command.CommandBuilderDispatcher;
import it.monopoly.controller.event.EventDispatcher;
import it.monopoly.controller.player.PlayerController;

public class ChanceSpaceDispenser extends SpaceDispenser {

    private final DrawableCardController drawableCardController;
    private final PlayerController playerController;
    private final EventDispatcher eventDispatcher;

    public ChanceSpaceDispenser(CommandBuilderDispatcher commandBuilderDispatcher,
                                DrawableCardController drawableCardController,
                                PlayerController playerController,
                                EventDispatcher eventDispatcher,
                                int... spaceIndex) {
        super(commandBuilderDispatcher, spaceIndex);
        this.drawableCardController = drawableCardController;
        this.playerController = playerController;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    protected Space getSpaceInstance() {
        return new ChanceSpace(commandBuilderDispatcher, drawableCardController, playerController, eventDispatcher);
    }
}
