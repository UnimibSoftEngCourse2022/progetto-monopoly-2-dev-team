package controller.board.space;

import controller.command.CommandBuilderDispatcher;
import model.player.PlayerModel;

import java.util.logging.Logger;

public class CornerSpace extends AbstractSpace {

    public CornerSpace(CommandBuilderDispatcher commandBuilderDispatcher) {
        super(commandBuilderDispatcher);
    }

    @Override
    public void applyEffect(PlayerModel player) {
        Logger.getLogger("Corne Space");
    }
}
