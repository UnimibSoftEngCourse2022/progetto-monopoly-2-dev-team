package controller.board.space;

import controller.card.DrawableCardController;
import controller.command.CommandBuilderDispatcher;
import controller.player.PlayerController;
import model.DrawableCardModel;

public class CommunityChestSpace extends DrawableCardSpace {

    public CommunityChestSpace(CommandBuilderDispatcher commandBuilderDispatcher, DrawableCardController drawableCardController, PlayerController playerController) {
        super(commandBuilderDispatcher, drawableCardController, playerController);
    }

    @Override
    protected DrawableCardModel draw() {
        return drawableCardController.draw(DrawableCardController.Type.COMMUNITY_CHEST);
    }
}
