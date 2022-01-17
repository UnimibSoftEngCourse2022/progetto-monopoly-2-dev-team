package model.player;

import util.Pair;

public class PlayerMovement {
    private final Pair<Integer, Integer> positionPair;
    private final boolean direct;

    public PlayerMovement(int previousPosition, int newPosition, boolean direct) {
        this.positionPair = new Pair<>(previousPosition, newPosition);
        this.direct = direct;
    }

    public Pair<Integer, Integer> getMovement() {
        return positionPair;
    }

    public boolean isDirect() {
        return direct;
    }
}
