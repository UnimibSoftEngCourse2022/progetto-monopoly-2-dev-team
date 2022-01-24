package it.monopoly.controller.player.command;

import it.monopoly.controller.command.Command;
import it.monopoly.controller.command.CommandBuilder;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.model.player.PlayerModel;

public class EndTurnCommandBuilder implements CommandBuilder {
    private final PlayerController playerController;
    private PlayerModel player;

    public EndTurnCommandBuilder(PlayerController playerController) {
        this.playerController = playerController;
    }

    public EndTurnCommandBuilder setPlayer(PlayerModel player) {
        this.player = player;
        return this;
    }

    @Override
    public Command build() {
        return new EndTurnCommand(playerController, player);
    }
}
