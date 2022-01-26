package it.monopoly.controller.event;

import it.monopoly.Broadcaster;
import it.monopoly.controller.ViewController;
import it.monopoly.controller.event.callback.BuyOrAuctionCallback;
import it.monopoly.controller.event.callback.DiceRollEventCallback;
import it.monopoly.controller.event.callback.UsePointsCallback;
import it.monopoly.manager.AbstractOfferManager;
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
                             BuyOrAuctionCallback callback) {
        viewController.getView(player).showBuyPropertyDialog(propertyModel, callback);
    }

    @Override
    public void useLoyaltyPoints(PlayerModel player, UsePointsCallback usePointsCallback) {
        //TODO Ask player to use points
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
