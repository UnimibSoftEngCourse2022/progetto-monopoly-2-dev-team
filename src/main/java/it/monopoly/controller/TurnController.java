package it.monopoly.controller;

import it.monopoly.controller.event.EventDispatcher;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.manager.player.PlayerManager;
import it.monopoly.manager.randomizer.RandomizationManager;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.player.ReadablePlayerModel;
import it.monopoly.view.Observer;

public class TurnController implements Observer<ReadablePlayerModel> {
    private final PlayerController playerController;
    private final EventDispatcher eventDispatcher;
    private final RandomizationManager randomizationManager;
    private int currentPlayerIndex = -1;
    private PlayerModel currentPlayer;

    public TurnController(PlayerController playerController,
                          EventDispatcher eventDispatcher,
                          RandomizationManager randomizationManager) {
        this.playerController = playerController;
        this.eventDispatcher = eventDispatcher;
        this.randomizationManager = randomizationManager;
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
            randomizationManager.randomize();
        }
    }

    private void notifyPlayerTurn() {
        PlayerModel currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            nextTurn();
            return;
        }
        PlayerManager manager = playerController.getManager(currentPlayer);
        if (manager != null) {
            boolean tookTurn = manager.startTurn();
            if (!tookTurn) {
                nextTurn();
            } else {
                eventDispatcher.sendMessage(currentPlayer.getName() + " turn");
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
