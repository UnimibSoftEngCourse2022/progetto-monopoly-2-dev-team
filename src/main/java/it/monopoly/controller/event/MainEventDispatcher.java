package it.monopoly.controller.event;

import it.monopoly.Broadcaster;
import it.monopoly.controller.TradeController;
import it.monopoly.manager.AuctionManager;
import it.monopoly.model.AuctionModel;
import it.monopoly.model.property.PropertyModel;
import it.monopoly.view.Observer;

import java.util.Collection;

public class MainEventDispatcher implements EventDispatcher {
    private final Broadcaster broadcaster;
    private final TradeController tradeController;

    public MainEventDispatcher(Broadcaster broadcaster, TradeController tradeController) {
        this.broadcaster = broadcaster;
        this.tradeController = tradeController;
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
    public void startAuction(PropertyModel property) {
        AuctionManager auctionManager = new AuctionManager(property, tradeController);
        broadcaster.startAuction(auctionManager);
        auctionManager.register(obj -> {
            if (auctionManager.hasEnded()) {
                broadcaster.endAuction(auctionManager);
            }
        });
    }
}
