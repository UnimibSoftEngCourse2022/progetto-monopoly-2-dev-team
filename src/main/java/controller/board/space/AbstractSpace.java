package controller.board.space;

import controller.command.CommandBuilderDispatcher;
import model.player.PlayerModel;

public abstract class AbstractSpace implements Space {
    protected final CommandBuilderDispatcher commandBuilderDispatcher;

    protected AbstractSpace(CommandBuilderDispatcher commandBuilderDispatcher) {
        this.commandBuilderDispatcher = commandBuilderDispatcher;
    }

    @Override
    public abstract void applyEffect(PlayerModel player);
}
