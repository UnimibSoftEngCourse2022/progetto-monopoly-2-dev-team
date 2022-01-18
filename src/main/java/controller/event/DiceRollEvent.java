package controller.event;

import util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DiceRollEvent implements DiceRoller {
    private final List<DiceRollEventCallback> callbacks = new ArrayList<>();

    public void registerCallback(DiceRollEventCallback callback) {
        if (callback != null) {
            callbacks.add(callback);
        }
    }

    public void deregisterCallback(DiceRollEventCallback callback) {
        if (callback != null) {
            callbacks.remove(callback);
        }
    }

    public Pair<Integer, Integer> rollDice() {
        Random random = new Random();
        int first = random.nextInt(6) + 1;
        int second = random.nextInt(6) + 1;
        Pair<Integer, Integer> result = new Pair<>(first, second);
        for (DiceRollEventCallback callback : callbacks) {
            callback.diceRolled(result);
        }
        return result;
    }
}
