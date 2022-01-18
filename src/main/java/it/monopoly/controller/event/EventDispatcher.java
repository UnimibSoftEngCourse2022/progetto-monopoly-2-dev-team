package it.monopoly.controller.event;

public interface EventDispatcher {
    DiceRoller diceRollEvent();

    DiceRoller diceRollEvent(DiceRollEventCallback callback);
}
