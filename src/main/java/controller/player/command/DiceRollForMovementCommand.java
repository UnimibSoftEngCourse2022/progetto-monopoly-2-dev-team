package controller.player.command;

import controller.command.Command;
import controller.event.EventDispatcher;
import controller.player.PlayerController;
import model.player.PlayerModel;
import util.Pair;

public class DiceRollForMovementCommand implements Command {
    private final PlayerController playerController;
    private final EventDispatcher eventDispatcher;
    private final PlayerModel player;
    private final MoveCommandBuilder moveCommandBuilder;

    private int doubleCount = 0;
    private boolean didDouble = false;
    private int result = 0;

    public DiceRollForMovementCommand(PlayerController playerController, EventDispatcher eventDispatcher, PlayerModel player, MoveCommandBuilder moveCommandBuilder) {
        this.playerController = playerController;
        this.eventDispatcher = eventDispatcher;
        this.player = player;
        this.moveCommandBuilder = moveCommandBuilder;
    }

    @Override
    public String getCommandName() {
        return "Roll dice";
    }

    @Override
    public boolean isEnabled() {
        return playerController.getManager(player).canTakeTurn();
    }

    @Override
    public void execute() {
        do {
            Pair<Integer, Integer> pair = eventDispatcher.diceRollEvent().rollDice();
            count(pair.getFirst(), pair.getSecond());
        } while (didDouble && doubleCount < 3);
        moveCommandBuilder
                .setGoToJail(doubleCount == 3)
                .setSpace(MoveCommand.Type.MOVE_OF, result)
                .setPlayer(player)
                .build()
                .execute();
    }

    private void count(int first, int second) {
        didDouble = first == second;
        if (didDouble) {
            doubleCount++;
        }
        result += first + second;
    }
}
