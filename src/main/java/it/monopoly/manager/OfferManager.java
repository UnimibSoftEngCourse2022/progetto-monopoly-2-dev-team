package it.monopoly.manager;

import it.monopoly.model.OfferModel;
import it.monopoly.model.player.PlayerModel;

import java.util.Collection;

public interface OfferManager {
    void makeOffer(PlayerModel player, int amount);

    Collection<OfferModel> getOffers();

    int getBestOffer();

    boolean hasEnded();
}
