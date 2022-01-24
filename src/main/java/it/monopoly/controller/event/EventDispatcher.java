package it.monopoly.controller.event;

import it.monopoly.manager.AbstractOfferManager;

public interface EventDispatcher {
    DiceRoller diceRollEvent();

    DiceRoller diceRollEvent(DiceRollEventCallback callback);

    void startOffer(AbstractOfferManager offerManager);

    void sendMessage(String message);
}
