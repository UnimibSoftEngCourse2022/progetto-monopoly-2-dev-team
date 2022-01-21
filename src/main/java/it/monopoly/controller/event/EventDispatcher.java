package it.monopoly.controller.event;

import it.monopoly.model.property.PropertyModel;

public interface EventDispatcher {
    DiceRoller diceRollEvent();

    DiceRoller diceRollEvent(DiceRollEventCallback callback);

    void startAuction(PropertyModel property);
}
