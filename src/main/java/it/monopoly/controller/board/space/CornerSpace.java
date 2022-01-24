package it.monopoly.controller.board.space;

import it.monopoly.controller.command.CommandBuilderDispatcher;
import it.monopoly.model.player.PlayerModel;

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
