package it.monopoly.controller;

import it.monopoly.controller.player.PlayerController;
import it.monopoly.manager.player.PlayerManager;
import it.monopoly.model.player.PlayerModel;

public class TurnController {
    private final PlayerController playerController;
    private int currentPlayerIndex = 0;

    public TurnController(PlayerController playerController) {
        this.playerController = playerController;
    }

    private void nextTurn() {
        currentPlayerIndex = ++currentPlayerIndex % playerController.getModels().size();
        notifyPlayerTurn();
    }

    private void notifyPlayerTurn() {
//        PlayerManager manager = playerController.getManager(getCurrentPlayer());
//        boolean tookTurn = manager.startTurn();
//        if(!tookTurn) {
//            nextTurn();
//        }
    }

    private PlayerModel getCurrentPlayer() {
        return playerController.getModels().get(currentPlayerIndex);
    }
}
