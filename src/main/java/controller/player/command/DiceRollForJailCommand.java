package controller.player.command;

import controller.command.Command;
import controller.event.EventDispatcher;
import controller.player.PlayerController;
import model.player.PlayerModel;
import util.Pair;

public class DiceRollForJailCommand implements Command {
    private final PlayerController playerController;
    private final EventDispatcher eventDispatcher;
    private final PlayerModel player;
    private final MoveCommandBuilder moveCommandBuilder;
    private PayCommandBuilder payCommandBuilder;

    public DiceRollForJailCommand(PlayerController playerController,
                                  EventDispatcher eventDispatcher,
                                  PlayerModel player,
                                  MoveCommandBuilder moveCommandBuilder,
                                  PayCommandBuilder payCommandBuilder) {
        this.playerController = playerController;
        this.eventDispatcher = eventDispatcher;
        this.player = player;
        this.moveCommandBuilder = moveCommandBuilder;
        this.payCommandBuilder = payCommandBuilder;
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
        Pair<Integer, Integer> pair = eventDispatcher.diceRollEvent().rollDice();
        count(pair.getFirst(), pair.getSecond());
    }

    private void count(int first, int second) {
        boolean succeeded;
        int result = first + second;
        if (first == second) {
            playerController.getManager(player).getOutOfJail();
            succeeded = true;
        } else {
            succeeded = playerController.getManager(player).tryToGetOutOfJail();
            if (succeeded) {
                payCommandBuilder.addDebtor(player)
                        .setMoney(50)
                        .build()
                        .execute();
            }
        }
        if (succeeded) {
            moveCommandBuilder
                    .setSpace(MoveCommand.Type.MOVE_OF, result)
                    .setPlayer(player)
                    .build()
                    .execute();
        }
    }
}
