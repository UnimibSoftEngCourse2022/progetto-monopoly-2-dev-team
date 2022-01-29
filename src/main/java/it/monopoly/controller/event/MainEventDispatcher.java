package it.monopoly.controller.event;

import it.monopoly.Broadcaster;
import it.monopoly.controller.ViewController;
import it.monopoly.controller.event.callback.DiceRollEventCallback;
import it.monopoly.controller.event.callback.FirstOrSecondCallback;
import it.monopoly.controller.event.callback.FirstSecondChoice;
import it.monopoly.manager.AbstractOfferManager;
import it.monopoly.manager.loyaltyprogram.LoyaltyProgram;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.ReadablePropertyModel;

public class MainEventDispatcher implements EventDispatcher {
    private final Broadcaster broadcaster;
    private final ViewController viewController;

    public MainEventDispatcher(Broadcaster broadcaster,
                               ViewController viewController) {
        this.broadcaster = broadcaster;
        this.viewController = viewController;
    }

    public DiceRoller diceRollEvent() {
        return diceRollEvent(null);
    }

    public DiceRoller diceRollEvent(DiceRollEventCallback callback) {
        DiceRollEvent event = new DiceRollEvent();
        if (callback != null) {
            event.registerCallback(callback);
        }
        return event;
    }

    @Override
    public void startOffer(AbstractOfferManager offerManager) {
        broadcaster.startOffers(offerManager);
        offerManager.register(obj -> {
            if (offerManager.hasEnded()) {
                broadcaster.endOffers(offerManager);
            }
        });
    }

    @Override
    public void buyOrAuction(PlayerModel player,
                             ReadablePropertyModel propertyModel,
                             FirstOrSecondCallback callback) {
        viewController.getView(player).showYesNoDialog(propertyModel.toString(), "Buy", "Start Auction", callback);
    }

    @Override
    public void useLoyaltyPoints(PlayerModel player, LoyaltyProgram loyalty, FirstOrSecondCallback callback) {
        if (loyalty.getValue().equals("0")) {
            callback.choose(FirstSecondChoice.SECOND);
            return;
        }
        viewController.getView(player).showYesNoDialog(
                "You have " + loyalty.getValue() + " points",
                "Spend points",
                "Do not use points (gather points)",
                callback
        );
    }

    @Override
    public void jailOrFine(PlayerModel player, FirstOrSecondCallback callback) {
        String text = "You are in jail\nDo you want to try to get out by throwing doubles in one of the next three turns or pay a fine (50) for the next two turns?";
        viewController.getView(player).showYesNoDialog(text, "Stay in jail", "Get out and pay fine", callback);
    }

    @Override
    public void showDialog(PlayerModel player, String message) {
        viewController.getView(player).showOkDialog(message);
    }

    @Override
    public void sendMessage(String message) {
        broadcaster.broadcastMessage(message);
    }
}
