package it.monopoly.controller.event;

import it.monopoly.util.Pair;

public interface DiceRoller {
    Pair<Integer, Integer> rollDice();
}
