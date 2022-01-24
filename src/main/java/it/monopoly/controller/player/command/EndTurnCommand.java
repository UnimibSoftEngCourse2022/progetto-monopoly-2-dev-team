package it.monopoly.controller.player.command;

import it.monopoly.controller.command.Command;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.manager.player.PlayerManager;
import it.monopoly.model.player.PlayerModel;

public class EndTurnCommand implements Command {
    private final PlayerController playerController;
    private final PlayerModel player;

    public EndTurnCommand(PlayerController playerController, PlayerModel player) {
        this.playerController = playerController;
        this.player = player;
    }

    @Override
    public String getCommandName() {
        return "End Turn";
    }

    @Override
    public boolean isEnabled() {
        PlayerManager manager = playerController.getManager(player);
        return manager != null && manager.canEndTurn();
    }

    @Override
    public void execute() {
        playerController.getManager(player).endTurn();
    }
}
