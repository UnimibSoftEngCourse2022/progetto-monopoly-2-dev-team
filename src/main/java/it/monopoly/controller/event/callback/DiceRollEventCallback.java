package it.monopoly.controller.event.callback;

import it.monopoly.util.Pair;

public interface DiceRollEventCallback {
    void diceRolled(Pair<Integer, Integer> result);
}
