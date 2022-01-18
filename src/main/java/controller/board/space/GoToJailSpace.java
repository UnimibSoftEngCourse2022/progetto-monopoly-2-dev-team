package controller.board.space;

import controller.command.CommandBuilderDispatcher;
import model.player.PlayerModel;

public class GoToJailSpace extends AbstractSpace {

    public GoToJailSpace(CommandBuilderDispatcher commandBuilderDispatcher) {
        super(commandBuilderDispatcher);
    }

    @Override
    public void applyEffect(PlayerModel player) {

    }
}
