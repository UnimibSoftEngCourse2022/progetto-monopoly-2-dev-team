package it.monopoly.controller.player.command;

import it.monopoly.controller.command.Command;
import it.monopoly.controller.event.EventDispatcher;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.manager.player.PlayerManager;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DiceRollForMovementCommand implements Command {
    private final Logger logger = LogManager.getLogger(getClass());
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
        PlayerManager manager = playerController.getManager(player);
        return manager != null && manager.canTakeTurn() && !manager.hasRolledDice();
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
        didDouble = first == second;
        if (didDouble) {
            doubleCount++;
            logger.info("Dice rolled double, re-rolling dice");
        }
        result += first + second;
    }
}
