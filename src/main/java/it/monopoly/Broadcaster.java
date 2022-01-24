package it.monopoly;

import it.monopoly.manager.AbstractOfferManager;
import it.monopoly.model.player.ReadablePlayerModel;
import it.monopoly.view.Observer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Broadcaster {
    private final Logger logger = LogManager.getLogger(getClass());
    private final Executor executor = Executors.newSingleThreadExecutor();

    private final List<Consumer<String>> messageListeners = new LinkedList<>();
    private final List<Consumer<ReadablePlayerModel>> playerListeners = new LinkedList<>();
    private final List<Consumer<AbstractOfferManager>> offersListeners = new LinkedList<>();
    private final Observer<ReadablePlayerModel> playerObserver = this::notifyAllPlayers;
    private AbstractOfferManager offerManager;

    public Observer<ReadablePlayerModel> getPlayerObserver() {
        return playerObserver;
    }

    public void broadcastMessage(String message) {
        for (Consumer<String> listener : messageListeners) {
            listener.accept(message);
        }
    }

    public void notifyAllPlayers(ReadablePlayerModel readablePlayers) {
        logger.debug("Notifying all players update");
        executor.execute(() -> {
            for (Consumer<ReadablePlayerModel> playerListener : playerListeners) {
                playerListener.accept(readablePlayers);
            }
        });
    }

    public void startOffers(AbstractOfferManager offerManager) {
        if (this.offerManager != null) {
            logger.error("Sell/Auction already started");
            return;
        }
        logger.debug("Notifying all players for auction/sell start");
        executor.execute(() -> {
            this.offerManager = offerManager;
            for (Consumer<AbstractOfferManager> listener : offersListeners) {
                listener.accept(offerManager);
            }
        });
    }

    public void endOffers(AbstractOfferManager offerManager) {
        if (offerManager == null) {
            logger.error("No auction/sell going");
            return;
        }
        logger.debug("Notifying all players for auction/sell end");
        executor.execute(() -> {
            for (Consumer<AbstractOfferManager> offersEndListener : offersListeners) {
                offersEndListener.accept(offerManager);
            }
            this.offerManager = null;
        });
    }

    public void registerForPlayers(Consumer<ReadablePlayerModel> consumer) {
        if (consumer != null) {
            playerListeners.add(consumer);
        }
    }

    public void deregisterForPlayers(Consumer<ReadablePlayerModel> consumer) {
        if (consumer != null) {
            playerListeners.remove(consumer);
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

    public void registerForMessages(Consumer<String> messageConsumer) {
        if (messageConsumer != null) {
            messageListeners.add(messageConsumer);
        }
    }

    public void deregisterForMessages(Consumer<String> messageConsumer) {
        if (messageConsumer != null) {
            offersListeners.remove(messageConsumer);
        }
    }
}
