package controller.board.space;

import controller.command.CommandBuilderDispatcher;

public abstract class AbstractSpace implements Space {
    protected final CommandBuilderDispatcher commandBuilderDispatcher;

    protected AbstractSpace(CommandBuilderDispatcher commandBuilderDispatcher) {
        this.commandBuilderDispatcher = commandBuilderDispatcher;
    }
}
