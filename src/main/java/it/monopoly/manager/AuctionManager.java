package it.monopoly.manager;

import it.monopoly.controller.TradeController;
import it.monopoly.model.AuctionModel;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyModel;
import it.monopoly.view.Observable;
import it.monopoly.view.Observer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.*;

public class AuctionManager implements Observable<Collection<AuctionModel>> {
    private final Logger logger = LogManager.getLogger(getClass());
    private boolean hasEnded = false;
    private final PropertyModel property;
    private final TradeController tradeController;
    private final List<Observer<Collection<AuctionModel>>> observers = new LinkedList<>();
    private final Map<PlayerModel, AuctionModel> playerOfferMap = new ConcurrentHashMap<>();
    private int maxOffer;
    private final ScheduledExecutorService executor;
    private ScheduledFuture<?> scheduledFuture;

    public AuctionManager(PropertyModel property, TradeController tradeController) {
        this.property = property;
        this.tradeController = tradeController;

        executor = Executors.newSingleThreadScheduledExecutor();
    }

    public synchronized void makeOffer(PlayerModel player, int amount) {
        if (scheduledFuture == null) {
            resetTimer();
            maxOffer = amount;
        } else if (amount > maxOffer) {
            maxOffer = amount;
            resetTimer();
        }

        playerOfferMap.put(player, new AuctionModel(player, amount));

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

    private void notifyObservers() {
        Collection<AuctionModel> offers = getOffers();
        for (Observer<Collection<AuctionModel>> observer : observers) {
            observer.notify(offers);
        }
    }


    public Collection<AuctionModel> getOffers() {
        return playerOfferMap.values();
    }

    public int getBestOffer() {
        return maxOffer;
    }

    private AuctionModel getBestAuctionModelOffer() {
        return playerOfferMap
                .values()
                .stream()
                .max(Comparator.comparingInt(AuctionModel::getAmount))
                .orElse(null);
    }

    public boolean hasEnded() {
        return hasEnded;
    }

    @Override
    public void register(Observer<Collection<AuctionModel>> observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    @Override
    public void deregister(Observer<Collection<AuctionModel>> observer) {
        if (observer != null) {
            observers.remove(observer);
        }
    }

    private void endAuction() {
        hasEnded = true;
        AuctionModel bestOffer = getBestAuctionModelOffer();
        if (bestOffer != null) {
            logger.info("Auction ended, player {} got property {} for {} money", bestOffer.getPlayer().toString(), property.getName(), bestOffer.getAmount());
            tradeController.buyPropertyAfterAuction(bestOffer.getPlayer(), property, bestOffer.getAmount());
        }
        notifyObservers();
        observers.clear();
    }
}
