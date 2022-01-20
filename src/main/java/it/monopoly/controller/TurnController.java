package it.monopoly.controller;

import it.monopoly.controller.player.PlayerController;
import it.monopoly.manager.player.PlayerManager;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.player.ReadablePlayerModel;
import it.monopoly.view.Observer;

import java.util.Iterator;

public class TurnController implements Observer<ReadablePlayerModel> {
    private final PlayerController playerController;
    private Iterator<PlayerModel> playerIterator;
    private PlayerModel currentPlayer;

    public TurnController(PlayerController playerController) {
        this.playerController = playerController;
    }

    public void start() {
        if (playerIterator == null) {
            playerIterator = playerController.getModels().iterator();
            nextTurn();
        }
    }

    private synchronized void nextTurn() {
        synchronized (playerController.getModels()) {
            if (playerIterator == null) {
                return;
            }
            if (!playerIterator.hasNext()) {
                playerIterator = playerController.getModels().iterator();
            }
            currentPlayer = playerIterator.next();
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
        currentPlayer = null;
        playerIterator = null;
    }
}
