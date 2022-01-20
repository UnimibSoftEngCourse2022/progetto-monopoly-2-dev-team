package it.monopoly.controller.player.command;

import it.monopoly.controller.command.Command;
import it.monopoly.controller.event.EventDispatcher;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.manager.player.PlayerManager;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DiceRollForJailCommand implements Command {
    private final Logger logger = LogManager.getLogger(getClass());
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
        PlayerManager manager = playerController.getManager(player);
        return manager != null && manager.canTakeTurn() && !manager.hasRolledDice();
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
