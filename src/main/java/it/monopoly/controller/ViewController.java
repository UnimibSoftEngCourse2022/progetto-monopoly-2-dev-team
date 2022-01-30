package it.monopoly.controller;

import it.monopoly.controller.board.PlayerPosition;
import it.monopoly.manager.AbstractOfferManager;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.player.ReadablePlayerModel;
import it.monopoly.view.MainView;
import it.monopoly.view.Observer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ViewController {
    private final Map<PlayerModel, MainView> playerViewMap = new ConcurrentHashMap<>();

    public void setUp(PlayerModel player, MainView view) {
        playerViewMap.put(player, view);
    }

    public MainView getView(PlayerModel player) {
        return playerViewMap.get(player);
    }

    public MainView remove(PlayerModel player) {
        return playerViewMap.remove(player);
    }

    public Consumer<AbstractOfferManager> getOfferConsumer(PlayerModel player) {
        return playerViewMap.get(player).getOfferConsumer();
    }

    public Consumer<ReadablePlayerModel> getAllPlayersConsumer(PlayerModel player) {
        return playerViewMap.get(player).getAllPlayerConsumer();
    }

    public Consumer<String> getMessageConsumer(PlayerModel player) {
        return playerViewMap.get(player).getMessageConsumer();
    }

    public Consumer<PlayerModel> getWinnerConsumer(PlayerModel player) {
        return playerViewMap.get(player).getWinnerConsumer();
    }

    public Observer<ReadablePlayerModel> getPlayerObserver(PlayerModel player) {
        return playerViewMap.get(player).getObserver(ReadablePlayerModel.class);
    }

    public Observer<PlayerPosition> getPlayerPositionObserver(PlayerModel player) {
        return playerViewMap.get(player).getObserver(PlayerPosition.class);
    }
}
