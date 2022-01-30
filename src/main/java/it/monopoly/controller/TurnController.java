package it.monopoly.controller;

import it.monopoly.controller.event.EventDispatcher;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.controller.property.PropertyController;
import it.monopoly.manager.player.PlayerManager;
import it.monopoly.manager.randomizer.RandomizationManager;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.player.ReadablePlayerModel;
import it.monopoly.view.Observer;

public class TurnController implements Observer<ReadablePlayerModel> {
    private boolean started = false;
    private final PlayerController playerController;
    private final EventDispatcher eventDispatcher;
    private final RandomizationManager randomizationManager;
    private final PlayerCheck playerCheck;
    private int currentPlayerIndex = -1;
    private PlayerModel currentPlayer;

    public TurnController(PropertyController propertyController, PlayerController playerController,
                          EventDispatcher eventDispatcher,
                          RandomizationManager randomizationManager) {
        this.playerController = playerController;
        this.eventDispatcher = eventDispatcher;
        this.randomizationManager = randomizationManager;
        this.playerCheck = new PlayerCheck(propertyController, playerController);
    }

    public void start() {
        synchronized (playerController.getModels()) {
            if (!playerController.getModels().isEmpty()) {
                started = true;
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

    @Override
    public void notify(ReadablePlayerModel player) {
        if (!started) {
            return;
        }
        PlayerModel winner = playerCheck.checkPlayers();
        if (winner != null) {
            eventDispatcher.announceWinner(winner);
            stop();
            return;
        }
        if (currentPlayer != null) {
            PlayerManager manager = playerController.getManager(currentPlayer);
            if (manager != null && !manager.isTakingTurn()) {
                nextTurn();
            }
        } else {
            nextTurn();
        }
    }

    public void stop() {
        started = false;
        currentPlayerIndex = -1;
        currentPlayer = null;
    }
}
