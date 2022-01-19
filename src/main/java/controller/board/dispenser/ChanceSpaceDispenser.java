package controller.board.dispenser;

import controller.board.space.ChanceSpace;
import controller.board.space.Space;
import controller.card.DrawableCardController;
import controller.command.CommandBuilderDispatcher;
import controller.player.PlayerController;

public class ChanceSpaceDispenser extends SpaceDispenser {

    private DrawableCardController drawableCardController;
    private PlayerController playerController;

    public ChanceSpaceDispenser(CommandBuilderDispatcher commandBuilderDispatcher,
                                DrawableCardController drawableCardController,
                                PlayerController playerController,
                                int... spaceIndex) {
        super(commandBuilderDispatcher, spaceIndex);
        this.drawableCardController = drawableCardController;
        this.playerController = playerController;
    }

    @Override
    protected Space getSpaceInstance() {
        return new ChanceSpace(commandBuilderDispatcher, drawableCardController, playerController);
    }
}
