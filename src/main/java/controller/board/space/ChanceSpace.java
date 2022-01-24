package controller.board.space;

import controller.card.DrawableCardController;
import controller.command.CommandBuilderDispatcher;
import controller.player.PlayerController;
import model.DrawableCardModel;

public class ChanceSpace extends DrawableCardSpace {

    public ChanceSpace(CommandBuilderDispatcher commandBuilderDispatcher, DrawableCardController drawableCardController, PlayerController playerController) {
        super(commandBuilderDispatcher, drawableCardController, playerController);
    }

    protected DrawableCardModel draw() {
        return drawableCardController.draw(DrawableCardController.Type.CHANCE);
    }
}
