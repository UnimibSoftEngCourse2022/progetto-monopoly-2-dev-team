package it.monopoly.controller.event;

import it.monopoly.util.Pair;

public interface DiceRollEventCallback {
    void diceRolled(Pair<Integer, Integer> result);
}
