package it.monopoly.controller.event;

import it.monopoly.controller.event.callback.DiceRollEventCallback;
import it.monopoly.util.Pair;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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
        Random random;
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            random = new Random();
        }
        int first = random.nextInt(6) + 1;
        int second = random.nextInt(6) + 1;
        Pair<Integer, Integer> result = new Pair<>(first, second);
        for (DiceRollEventCallback callback : callbacks) {
            callback.diceRolled(result);
        }
        return result;
    }
}
