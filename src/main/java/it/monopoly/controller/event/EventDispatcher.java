package it.monopoly.controller.event;

import it.monopoly.controller.event.callback.BuyOrAuctionCallback;
import it.monopoly.controller.event.callback.DiceRollEventCallback;
import it.monopoly.manager.AbstractOfferManager;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.ReadablePropertyModel;

public interface EventDispatcher {
    DiceRoller diceRollEvent();

    DiceRoller diceRollEvent(DiceRollEventCallback callback);

    void startOffer(AbstractOfferManager offerManager);

    void buyOrAuction(PlayerModel player,
                      ReadablePropertyModel propertyModel,
                      BuyOrAuctionCallback callback);

    void sendMessage(String message);
}
