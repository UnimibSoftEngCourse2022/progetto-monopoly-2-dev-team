package it.monopoly.controller;

import it.monopoly.controller.player.PlayerController;
import it.monopoly.manager.player.PlayerManager;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.player.ReadablePlayerModel;
import it.monopoly.view.Observer;

public class TurnController implements Observer<ReadablePlayerModel> {
    private final PlayerController playerController;
    private PlayerModel currentPlayer = null;

    public TurnController(PlayerController playerController) {
        this.playerController = playerController;
    }

    public void start() {
        if (currentPlayer == null) {
            currentPlayer = playerController.getModels().get(0);
            notifyPlayerTurn();
        }
    }

    private void nextTurn() {
        int index = (playerController.getModels().indexOf(currentPlayer) + 1) % playerController.getModels().size();
        this.currentPlayer = playerController.getModels().get(index);
        notifyPlayerTurn();
    }

    private void notifyPlayerTurn() {
        PlayerManager manager = playerController.getManager(getCurrentPlayer());
        if (manager != null) {
            boolean tookTurn = manager.startTurn();
            if (!tookTurn) {
                nextTurn();
            }
        }
    }

    private PlayerModel getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public void notify(ReadablePlayerModel player) {
        PlayerManager manager = playerController.getManager(getCurrentPlayer());
        if (manager != null && !manager.isTakingTurn()) {
            nextTurn();
        }
    }

    public void stop() {
        currentPlayer = null;
    }
}
