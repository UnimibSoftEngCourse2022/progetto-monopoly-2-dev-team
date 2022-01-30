package it.monopoly.manager;

import it.monopoly.controller.TradeController;
import it.monopoly.model.OfferModel;
import it.monopoly.model.enums.OfferType;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyModel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class AuctionManager extends AbstractOfferManager {
    private final ScheduledExecutorService executor;
    private int maxOffer;
    private ScheduledFuture<?> scheduledFuture;

    public AuctionManager(PlayerModel player, PropertyModel property, TradeController tradeController) {
        super(OfferType.AUCTION, player, property, tradeController);
        executor = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public synchronized void makeOffer(PlayerModel player, int amount) {
        if (scheduledFuture == null) {
            resetTimer();
            maxOffer = amount;
        } else if (amount > maxOffer) {
            maxOffer = amount;
            resetTimer();
        }

        putNewOffer(player, amount);

        notifyObservers();
    }

    private synchronized void resetTimer() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        } else {
            logger.info("Auction started for property {}", property.getName());
        }
        scheduledFuture = executor.schedule(this::endAuction, 10, TimeUnit.SECONDS);
    }

    @Override
    public int getBestOffer() {
        return maxOffer;
    }

    private void endAuction() {
        Executors.newSingleThreadExecutor().execute(() -> {
            hasEnded = true;
            OfferModel bestOffer = getBestOfferModel();
            if (bestOffer != null) {
                logger.info("Auction ended, player {} got property {} for {} money", bestOffer.getPlayer(), property.getName(), bestOffer.getAmount());
                tradeController.buyPropertyAfterAuction(bestOffer.getPlayer(), property, bestOffer.getAmount());
            }
            notifyObservers();
            clearObservers();
        });
    }
}
