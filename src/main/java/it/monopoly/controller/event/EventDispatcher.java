package it.monopoly.controller.event;

import it.monopoly.manager.AbstractOfferManager;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyModel;

public interface EventDispatcher {
    DiceRoller diceRollEvent();

    DiceRoller diceRollEvent(DiceRollEventCallback callback);

    void startOffer(AbstractOfferManager offerManager);
}
