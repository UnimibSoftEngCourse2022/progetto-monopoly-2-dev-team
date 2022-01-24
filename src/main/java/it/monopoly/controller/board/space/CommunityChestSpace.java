package it.monopoly.controller.board.space;

import it.monopoly.controller.board.card.DrawableCardController;
import it.monopoly.controller.command.CommandBuilderDispatcher;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.model.DrawableCardModel;

public class CommunityChestSpace extends DrawableCardSpace {

    public CommunityChestSpace(CommandBuilderDispatcher commandBuilderDispatcher, DrawableCardController drawableCardController, PlayerController playerController) {
        super(commandBuilderDispatcher, drawableCardController, playerController);
    }

    @Override
    protected DrawableCardModel draw() {
        return drawableCardController.draw(DrawableCardController.Type.COMMUNITY_CHEST);
    }
}
