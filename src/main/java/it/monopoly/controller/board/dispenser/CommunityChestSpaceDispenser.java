package it.monopoly.controller.board.dispenser;

import it.monopoly.controller.board.card.DrawableCardController;
import it.monopoly.controller.board.space.CommunityChestSpace;
import it.monopoly.controller.board.space.Space;
import it.monopoly.controller.command.CommandBuilderDispatcher;
import it.monopoly.controller.player.PlayerController;

public class CommunityChestSpaceDispenser extends SpaceDispenser {

    private final DrawableCardController drawableCardController;
    private final PlayerController playerController;

    public CommunityChestSpaceDispenser(CommandBuilderDispatcher commandBuilderDispatcher,
                                        DrawableCardController drawableCardController,
                                        PlayerController playerController,
                                        int... spaceIndex) {
        super(commandBuilderDispatcher, spaceIndex);
        this.drawableCardController = drawableCardController;
        this.playerController = playerController;
    }

    @Override
    protected Space getSpaceInstance() {
        return new CommunityChestSpace(commandBuilderDispatcher, drawableCardController, playerController);
    }
}
