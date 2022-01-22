package it.monopoly.manager;

import it.monopoly.controller.TradeController;
import it.monopoly.model.OfferModel;
import it.monopoly.model.enums.OfferType;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyModel;

public class SellManager extends AbstractOfferManager {
    private final PlayerModel owner;
    private int maxOffer = -1;

    public SellManager(PlayerModel owner, PropertyModel property, TradeController tradeController) {
        super(OfferType.SELL, property, tradeController);
        this.owner = owner;
    }

    @Override
    public synchronized void makeOffer(PlayerModel player, int amount) {
        if (amount > maxOffer) {
            maxOffer = amount;
        }

        putNewOffer(player, amount);

        notifyObservers();
    }

    @Override
    public boolean isSuperVisor(PlayerModel player) {
        return player != null && player.equals(owner);
    }

    @Override
    public int getBestOffer() {
        return maxOffer;
    }

    @Override
    public void endOffers() {
        endOffers(getBestOfferModel());
    }

    @Override
    public void endOffers(OfferModel offer) {
        hasEnded = true;
        if (offer != null) {
            logger.info("Sell ended, player {} got property {} for {} money", offer.getPlayer().toString(), property.getName(), offer.getAmount());
            tradeController.buyPropertyFromPlayer(offer.getPlayer(), owner, property, offer.getAmount());
        }
        notifyObservers();
        clearObservers();
    }
}
