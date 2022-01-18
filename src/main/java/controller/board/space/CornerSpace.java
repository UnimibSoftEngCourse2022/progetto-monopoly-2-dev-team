package controller.board.space;

import controller.command.CommandBuilderDispatcher;
import model.player.PlayerModel;

public class CornerSpace extends AbstractSpace {

    public CornerSpace(CommandBuilderDispatcher commandBuilderDispatcher) {
        super(commandBuilderDispatcher);
    }

    @Override
    public void applyEffect(PlayerModel player) {

    }
}
