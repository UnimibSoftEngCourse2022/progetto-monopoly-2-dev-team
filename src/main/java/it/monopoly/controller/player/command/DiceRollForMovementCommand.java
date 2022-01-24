package it.monopoly.controller.player.command;

import it.monopoly.controller.event.EventDispatcher;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.util.Pair;

public class DiceRollForMovementCommand extends DiceRollCommand {

    private int doubleCount = 0;
    private boolean didDouble = false;
    private int result = 0;

    public DiceRollForMovementCommand(PlayerController playerController,
                                      EventDispatcher eventDispatcher,
                                      PlayerModel player,
                                      MoveCommandBuilder moveCommandBuilder) {
        super(playerController, eventDispatcher, player, moveCommandBuilder);
    }

    @Override
    public void execute() {
        logger.info("Executing DiceRollForMovementCommand");
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
        if (doubleCount == 3) {
            playerController.getManager(player).goToJail();
        }
        doubleCount = 0;
        result = 0;
        playerController.getManager(player).setDiceRolled();
    }


    private void count(int first, int second) {
        logger.info("Dice rolled {} and {}", first, second);
        eventDispatcher.sendMessage(player.getName() + " rolled " + first + " & " + second);
        didDouble = first == second;
        if (didDouble) {
            doubleCount++;
            logger.info("{} rolled double, re-rolling dice", player.getName());
            eventDispatcher.sendMessage("Re-rolling dice");
        }
        result += first + second;
    }
}
