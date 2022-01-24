package it.monopoly.controller.player.command;

import it.monopoly.controller.event.EventDispatcher;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.util.Pair;

public class DiceRollForJailCommand extends DiceRollCommand {

    private final PayCommandBuilder payCommandBuilder;

    public DiceRollForJailCommand(PlayerController playerController,
                                  EventDispatcher eventDispatcher,
                                  PlayerModel player,
                                  MoveCommandBuilder moveCommandBuilder,
                                  PayCommandBuilder payCommandBuilder) {
        super(playerController, eventDispatcher, player, moveCommandBuilder);
        this.payCommandBuilder = payCommandBuilder;
    }

    @Override
    public void execute() {
        logger.info("Executing DiceRollForMovementCommand");
        Pair<Integer, Integer> pair = eventDispatcher.diceRollEvent().rollDice();
        count(pair.getFirst(), pair.getSecond());
        playerController.getManager(player).setDiceRolled();
    }

    private void count(int first, int second) {
        logger.info("Dice rolled {} and {}", first, second);
        eventDispatcher.sendMessage(player.getName() + " rolled " + first + " & " + second);
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
            eventDispatcher.sendMessage(player.getName() + " got out of jail!");
            moveCommandBuilder
                    .setSpace(MoveCommand.Type.MOVE_OF, result)
                    .setPlayer(player)
                    .build()
                    .execute();
        }
    }
}
