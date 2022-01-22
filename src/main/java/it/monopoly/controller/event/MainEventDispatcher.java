package it.monopoly.controller.event;

import it.monopoly.Broadcaster;
import it.monopoly.controller.TradeController;
import it.monopoly.controller.ViewController;
import it.monopoly.manager.AbstractOfferManager;
import it.monopoly.manager.OfferManager;

public class MainEventDispatcher implements EventDispatcher {
    private final Broadcaster broadcaster;
    private final TradeController tradeController;
    private final ViewController viewController;

    public MainEventDispatcher(Broadcaster broadcaster,
                               TradeController tradeController, //TODO remove dependency by passing auction manager directly in startAuction method
                               ViewController viewController) {
        this.broadcaster = broadcaster;
        this.tradeController = tradeController;
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
}
