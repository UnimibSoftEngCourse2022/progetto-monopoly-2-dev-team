package it.monopoly;

import it.monopoly.controller.player.PlayerController;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.player.ReadablePlayerModel;
import it.monopoly.model.property.PropertyModel;
import it.monopoly.view.Observer;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Broadcaster {
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final PlayerController playerController;

    private final List<Consumer<PropertyModel>> propertyListeners = new LinkedList<>();
    private final List<Consumer<ReadablePlayerModel>> playerListeners = new LinkedList<>();
    private final List<Consumer<Runnable>> voidListeners = new LinkedList<>();
    private Observer<ReadablePlayerModel> playerObserver;

    public Broadcaster(PlayerController playerController) {
        this.playerController = playerController;
    }

    public void registerPlayerListener(Consumer<ReadablePlayerModel> consumer) {
        playerListeners.add(consumer);
    }

    public void deregisterPlayerListener(Consumer<ReadablePlayerModel> consumer) {
        playerListeners.remove(consumer);
    }

    public Observer<ReadablePlayerModel> getAllPlayersObserver() {
        if (playerObserver == null) {
            playerObserver = readablePlayer -> {
                for (Consumer<ReadablePlayerModel> listener : playerListeners) {
                    executor.execute(() -> listener.accept(readablePlayer));
                }
            };
        }
        return playerObserver;
    }

    public void getPlayerObserver(PlayerModel player, Consumer<ReadablePlayerModel> consumer) {
        if (player != null) {
            Observer<ReadablePlayerModel> observer =
                    readablePlayer -> executor.execute(() -> consumer.accept(readablePlayer));
            playerController.getManager(player).register(observer);
        }
    }

}
