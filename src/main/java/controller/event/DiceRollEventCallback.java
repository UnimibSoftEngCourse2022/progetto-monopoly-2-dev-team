package controller.event;

import util.Pair;

public interface DiceRollEventCallback {
    void diceRolled(Pair<Integer, Integer> result);
}
