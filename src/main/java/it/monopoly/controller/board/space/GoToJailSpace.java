package it.monopoly.controller.board.space;

import it.monopoly.controller.command.CommandBuilderDispatcher;
import it.monopoly.controller.player.command.MoveCommandBuilder;
import it.monopoly.model.player.PlayerModel;

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
