package it.monopoly.controller.player.command;

import it.monopoly.controller.command.Command;
import it.monopoly.controller.event.EventDispatcher;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.manager.player.PlayerManager;
import it.monopoly.model.player.PlayerModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class DiceRollCommand implements Command {
    protected final Logger logger = LogManager.getLogger(getClass());
    protected final PlayerController playerController;
    protected final EventDispatcher eventDispatcher;
    protected final PlayerModel player;
    protected final MoveCommandBuilder moveCommandBuilder;

    protected DiceRollCommand(PlayerController playerController,
                              EventDispatcher eventDispatcher,
                              PlayerModel player,
                              MoveCommandBuilder moveCommandBuilder) {
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
}
