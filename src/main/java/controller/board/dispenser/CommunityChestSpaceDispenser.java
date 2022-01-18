package controller.board.dispenser;

import controller.board.space.CommunityChestSpace;
import controller.board.space.Space;
import controller.command.CommandBuilderDispatcher;
import controller.card.DrawableCardController;

public class CommunityChestSpaceDispenser extends SpaceDispenser {

    private DrawableCardController drawableCardController;

    public CommunityChestSpaceDispenser(CommandBuilderDispatcher commandBuilderDispatcher,
                                        DrawableCardController drawableCardController,
                                        int... spaceIndex) {
        super(commandBuilderDispatcher, spaceIndex);
        this.drawableCardController = drawableCardController;
    }

    @Override
    protected Space getSpaceInstance() {
        return new CommunityChestSpace(commandBuilderDispatcher, drawableCardController);
    }
}
