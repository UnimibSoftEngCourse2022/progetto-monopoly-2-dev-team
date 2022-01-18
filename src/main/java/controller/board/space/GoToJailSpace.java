package controller.board.space;

import controller.command.Command;
import controller.command.CommandBuilderDispatcher;
import controller.player.command.MoveCommand;
import controller.player.command.MoveCommandBuilder;
import model.player.PlayerModel;

public class GoToJailSpace extends AbstractSpace {

    public GoToJailSpace(CommandBuilderDispatcher commandBuilderDispatcher) {
        super(commandBuilderDispatcher);
    }

    @Override
    public void applyEffect(PlayerModel player) {
        commandBuilderDispatcher
                .createCommandBuilder(MoveCommandBuilder.class)
                .setPlayer(player)
                .setGoToJail(true)
                .build()
                .execute();
    }
}
