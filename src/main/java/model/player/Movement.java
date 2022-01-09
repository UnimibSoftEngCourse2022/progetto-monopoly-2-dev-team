package model.player;

import util.Pair;

public class Movement {
    private final Pair<Integer, Integer> movement;
    private final boolean direct;

    public Movement(int previousPosition, int newPosition, boolean direct) {
        this.movement = new Pair<>(previousPosition, newPosition);
        this.direct = direct;
    }

    public Pair<Integer, Integer> getMovement() {
        return movement;
    }

    public boolean isDirect() {
        return direct;
    }
}