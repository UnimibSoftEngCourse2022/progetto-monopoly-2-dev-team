package controller.board.dispenser;

import controller.board.space.Space;
import controller.command.CommandBuilderDispatcher;

import java.util.stream.IntStream;

public abstract class SpaceDispenser {

    protected final CommandBuilderDispatcher commandBuilderDispatcher;
    protected SpaceDispenser successor;
    protected int[] spaceIndex;

    protected SpaceDispenser(CommandBuilderDispatcher commandBuilderDispatcher,
                             int... spaceIndex) {
        this.commandBuilderDispatcher = commandBuilderDispatcher;
        this.spaceIndex = spaceIndex;
    }

    public final Space processSpace(int spaceIndex) {
        if (successor == null || IntStream.of(this.spaceIndex).anyMatch(x -> x == spaceIndex)) {
            return getSpaceInstance();
        }
        return successor.processSpace(spaceIndex);
    }

    public SpaceDispenser setSuccessor(SpaceDispenser successor) {
        this.successor = successor;
        return successor;
    }

    protected abstract Space getSpaceInstance();

}
