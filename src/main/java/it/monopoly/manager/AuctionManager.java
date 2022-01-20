package it.monopoly.manager;

import it.monopoly.model.AuctionModel;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.view.Observable;
import it.monopoly.view.Observer;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuctionManager implements Observable<Collection<AuctionModel>> {
    private final List<Observer<Collection<AuctionModel>>> observers = new LinkedList<>();
    private final Map<PlayerModel, AuctionModel> playerOfferMap = new ConcurrentHashMap<>();

    public void makeOffer(PlayerModel player, int amount) {
        playerOfferMap.put(player, new AuctionModel(player, amount));
        notifyObservers();
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
        observers.clear();
    }
}
