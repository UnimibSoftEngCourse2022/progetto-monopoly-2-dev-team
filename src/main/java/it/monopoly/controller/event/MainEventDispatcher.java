package it.monopoly.controller.event;

import it.monopoly.Broadcaster;
import it.monopoly.controller.ViewController;
import it.monopoly.manager.AbstractOfferManager;

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
}
