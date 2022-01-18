package controller.board.space;

import controller.command.CommandBuilderDispatcher;
import model.player.PlayerModel;

public class ChanceSpace extends AbstractSpace {

    public ChanceSpace(CommandBuilderDispatcher commandBuilderDispatcher) {
        super(commandBuilderDispatcher);
    }

    @Override
    public void applyEffect(PlayerModel player) {

    }
}
