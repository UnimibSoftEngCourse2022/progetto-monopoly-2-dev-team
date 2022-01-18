package controller.board.space;

import controller.command.CommandBuilderDispatcher;
import model.player.PlayerModel;

public class TaxSpace extends AbstractSpace {

    public TaxSpace(CommandBuilderDispatcher commandBuilderDispatcher) {
        super(commandBuilderDispatcher);
    }

    @Override
    public void applyEffect(PlayerModel player) {

    }
}
