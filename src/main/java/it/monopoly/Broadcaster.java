package it.monopoly;

import it.monopoly.controller.player.PlayerController;
import it.monopoly.manager.AbstractOfferManager;
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
    private final List<Consumer<AbstractOfferManager>> offersListeners = new LinkedList<>();
    private AbstractOfferManager offerManager;
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

    public void startOffers(AbstractOfferManager offerManager) {
        if (this.offerManager != null) {
            return;
        }
        this.offerManager = offerManager;
        for (Consumer<AbstractOfferManager> listener : offersListeners) {
            listener.accept(offerManager);
        }
    }

    public void endOffers(AbstractOfferManager offerManager) {
        this.offerManager = null;
        for (Consumer<AbstractOfferManager> auctionEndListener : offersListeners) {
            auctionEndListener.accept(offerManager);
        }
    }

    public void registerForOffers(Consumer<AbstractOfferManager> offerManagerConsumer) {
        if (offerManagerConsumer != null) {
            offersListeners.add(offerManagerConsumer);
            if (offerManager != null) {
                offerManagerConsumer.accept(offerManager);
            }
        }
    }

    public void deregisterFromOffers(Consumer<AbstractOfferManager> offerManagerConsumer) {
        if (offerManagerConsumer != null) {
            offersListeners.remove(offerManagerConsumer);
        }
    }
}
