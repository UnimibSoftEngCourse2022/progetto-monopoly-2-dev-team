package it.monopoly.controller.event;

import it.monopoly.controller.event.callback.DiceRollEventCallback;
import it.monopoly.controller.event.callback.FirstOrSecondCallback;
import it.monopoly.manager.AbstractOfferManager;
import it.monopoly.manager.loyaltyprogram.LoyaltyProgram;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.ReadablePropertyModel;

public interface EventDispatcher {
    DiceRoller diceRollEvent();

    DiceRoller diceRollEvent(DiceRollEventCallback callback);

    void startOffer(AbstractOfferManager offerManager);

    void buyOrAuction(PlayerModel player,
                      ReadablePropertyModel propertyModel,
                      FirstOrSecondCallback callback);

    void useLoyaltyPoints(PlayerModel player, LoyaltyProgram loyalty, FirstOrSecondCallback callback);

    void jailOrFine(PlayerModel player, FirstOrSecondCallback callback);

    void showDialog(PlayerModel player, String message);

    void sendMessage(String message);
}
