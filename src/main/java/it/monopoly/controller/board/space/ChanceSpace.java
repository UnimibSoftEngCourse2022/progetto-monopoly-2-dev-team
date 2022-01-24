package it.monopoly.controller.board.space;

import it.monopoly.controller.board.card.DrawableCardController;
import it.monopoly.controller.command.CommandBuilderDispatcher;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.model.DrawableCardModel;

public class ChanceSpace extends DrawableCardSpace {

    public ChanceSpace(CommandBuilderDispatcher commandBuilderDispatcher, DrawableCardController drawableCardController, PlayerController playerController) {
        super(commandBuilderDispatcher, drawableCardController, playerController);
    }

    protected DrawableCardModel draw() {
        return drawableCardController.draw(DrawableCardController.Type.CHANCE);
    }
}
