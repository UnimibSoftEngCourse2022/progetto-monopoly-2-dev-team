package it.monopoly.manager;

import it.monopoly.controller.TradeController;
import it.monopoly.model.OfferModel;
import it.monopoly.model.enums.OfferType;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyModel;
import it.monopoly.view.Observable;
import it.monopoly.view.Observer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractOfferManager implements Observable<Collection<OfferModel>>, OfferManager {
    protected final Logger logger = LogManager.getLogger(getClass());
    protected boolean hasEnded = false;
    private final OfferType type;
    private final PlayerModel player;
    protected final PropertyModel property;
    protected final TradeController tradeController;
    private final List<Observer<Collection<OfferModel>>> observers = new LinkedList<>();
    protected final Map<PlayerModel, OfferModel> playerOfferMap = new ConcurrentHashMap<>();

    public AbstractOfferManager(OfferType type, PlayerModel player, PropertyModel property, TradeController tradeController) {
        this.type = type;
        this.player = player;
        this.property = property;
        this.tradeController = tradeController;
    }

    @Override
    public abstract void makeOffer(PlayerModel player, int amount);

    protected void putNewOffer(PlayerModel player, int amount) {
        playerOfferMap.put(player, new OfferModel(getType(), player, amount));
    }

    @Override
    public final Collection<OfferModel> getOffers() {
        return playerOfferMap.values();
    }

    @Override
    public abstract int getBestOffer();


    protected OfferModel getBestOfferModel() {
        return playerOfferMap
                .values()
                .stream()
                .max(Comparator.comparingInt(OfferModel::getAmount))
                .orElse(null);
    }

    @Override
    public final boolean hasEnded() {
        return hasEnded;
    }

    public boolean isSuperVisor(PlayerModel player) {
        return false;
    }

    public final PropertyModel getProperty() {
        return property;
    }

    public final String getPlayerName() {
        return player != null ? player.getName() : "Unknown";
    }

    public final OfferType getType() {
        return type;
    }

    public void endOffers() {
    }

    public void endOffers(OfferModel offer) {
    }

    protected void notifyObservers() {
        Collection<OfferModel> offers = getOffers();
        for (Observer<Collection<OfferModel>> observer : observers) {
            observer.notify(offers);
        }
    }

    @Override
    public final void register(Observer<Collection<OfferModel>> observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    @Override
    public final void deregister(Observer<Collection<OfferModel>> observer) {
        if (observer != null) {
            observers.remove(observer);
        }
    }

    protected final void clearObservers() {
        observers.clear();
    }
}
