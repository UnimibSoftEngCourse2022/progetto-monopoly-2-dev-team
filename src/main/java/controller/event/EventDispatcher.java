package controller.event;

public interface EventDispatcher {
    DiceRoller diceRollEvent();

    DiceRoller diceRollEvent(DiceRollEventCallback callback);
}
