package controller.event;

public class EventDispatcher {

    public void diceRollEvent(DiceRollEventCallback callback) {
        DiceRollEvent event = new DiceRollEvent();
        event.registerCallback(callback);
        event.roll();
    }


}
