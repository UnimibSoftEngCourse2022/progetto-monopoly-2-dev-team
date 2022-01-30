package ut;

import it.monopoly.controller.event.DiceRoller;
import it.monopoly.controller.event.EventDispatcher;
import it.monopoly.controller.event.callback.DiceRollEventCallback;
import it.monopoly.controller.event.callback.FirstOrSecondCallback;
import it.monopoly.manager.AbstractOfferManager;
import it.monopoly.manager.loyaltyprogram.LoyaltyProgram;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.ReadablePropertyModel;

public abstract class DummyEventDispatcher implements EventDispatcher {
    @Override
    public DiceRoller diceRollEvent() {
        return null;
    }

    @Override
    public DiceRoller diceRollEvent(DiceRollEventCallback callback) {
        return null;
    }

    @Override
    public void startOffer(AbstractOfferManager offerManager) {

    }

    @Override
    public void buyOrAuction(PlayerModel player, ReadablePropertyModel propertyModel, FirstOrSecondCallback callback) {

    }

    @Override
    public void useLoyaltyPoints(PlayerModel player, LoyaltyProgram loyalty, FirstOrSecondCallback callback) {

    }

    @Override
    public void jailOrFine(PlayerModel player, FirstOrSecondCallback callback) {

    }

    @Override
    public void showDialog(PlayerModel player, String message) {

    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void announceWinner(PlayerModel winner) {

    }
}
