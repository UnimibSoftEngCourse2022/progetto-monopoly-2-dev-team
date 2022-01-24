package controller.board.dispenser;

import controller.board.space.GoToJailSpace;
import controller.board.space.Space;
import controller.command.CommandBuilderDispatcher;
import controller.player.PlayerController;

public class GoToJailSpaceDispenser extends SpaceDispenser {

    private PlayerController playerController;

    public GoToJailSpaceDispenser(CommandBuilderDispatcher commandBuilderDispatcher,
                                  int... spaceIndex) {
        super(commandBuilderDispatcher, spaceIndex);
    }

    @Override
    protected Space getSpaceInstance() {
        return new GoToJailSpace(commandBuilderDispatcher, playerController);
    }
}
