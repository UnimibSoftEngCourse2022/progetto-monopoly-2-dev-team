package controller.board.dispenser;

import controller.board.space.CommunityChestSpace;
import controller.board.space.Space;
import controller.card.DrawableCardController;
import controller.command.CommandBuilderDispatcher;
import controller.player.PlayerController;

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
