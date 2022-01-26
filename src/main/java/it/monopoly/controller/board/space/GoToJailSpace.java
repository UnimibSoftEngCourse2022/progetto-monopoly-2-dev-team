package it.monopoly.controller.board.space;

import it.monopoly.controller.command.CommandBuilderDispatcher;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.controller.player.command.MoveCommandBuilder;
import it.monopoly.model.player.PlayerModel;

public class GoToJailSpace extends AbstractSpace {

    private final PlayerController playerController;

    public GoToJailSpace(CommandBuilderDispatcher commandBuilderDispatcher,
                         PlayerController playerController) {
        super(commandBuilderDispatcher);
        this.playerController = playerController;
    }

    @Override
    public void applyEffect(PlayerModel player) {
        commandBuilderDispatcher
                .createCommandBuilder(MoveCommandBuilder.class)
                .setPlayer(player)
                .setGoToJail(true)
                .build()
                .execute();
        playerController.getManager(player).goToJail();
    }
}
