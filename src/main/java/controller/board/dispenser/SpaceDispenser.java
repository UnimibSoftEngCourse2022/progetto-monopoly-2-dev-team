package controller.board.dispenser;

import controller.board.space.Space;

import java.util.stream.IntStream;

public abstract class SpaceDispenser {

    protected SpaceDispenser successor;
    protected int[] spaceIndex;

    public final Space processSpace(int spaceIndex) {
        if (successor == null || IntStream.of(this.spaceIndex).anyMatch(x -> x == spaceIndex)) {
            return getSpaceInstance();
        }
        return successor.processSpace(spaceIndex);
    }

    protected SpaceDispenser(int... spaceIndex) {
        this.spaceIndex = spaceIndex;
    }

    public SpaceDispenser setSuccessor(SpaceDispenser successor) {
        this.successor = successor;
        return successor;
    }

    protected abstract Space getSpaceInstance();

}
