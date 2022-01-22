package it.monopoly.controller;

import it.monopoly.controller.player.PlayerController;
import it.monopoly.manager.player.PlayerManager;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.player.ReadablePlayerModel;
import it.monopoly.view.Observer;

public class TurnController implements Observer<ReadablePlayerModel> {
    private final PlayerController playerController;
    private int currentPlayerIndex = -1;
    private PlayerModel currentPlayer;

    public TurnController(PlayerController playerController) {
        this.playerController = playerController;
    }

    public void start() {
        synchronized (playerController.getModels()) {
            if (!playerController.getModels().isEmpty()) {
                nextTurn();
            }
        }
    }

    private synchronized void nextTurn() {
        synchronized (playerController.getModels()) {
            if (!playerController.getModels().isEmpty()) {
                int index = currentPlayer == null ? -1 : playerController.getModels().indexOf(currentPlayer);
                if (index == -1) {
                    index = currentPlayerIndex + 1;
                } else {
                    index++;
                }
                currentPlayerIndex = index % playerController.getModels().size();
            }
            currentPlayer = playerController.getModels().get(currentPlayerIndex);
            notifyPlayerTurn();
        }
    }

    private void notifyPlayerTurn() {
        if (getCurrentPlayer() == null) {
            nextTurn();
        }
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
    public synchronized void notify(ReadablePlayerModel player) {
        if (getCurrentPlayer() != null) {
            PlayerManager manager = playerController.getManager(getCurrentPlayer());
            if (manager != null && !manager.isTakingTurn()) {
                nextTurn();
            }
        } else {
            nextTurn();
        }
    }

    public void stop() {
        currentPlayerIndex = -1;
        currentPlayer = null;
    }
}
