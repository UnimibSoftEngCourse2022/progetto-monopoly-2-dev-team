package it.monopoly.controller.player.command;

import it.monopoly.controller.command.Command;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.manager.player.PlayerManager;
import it.monopoly.model.player.PlayerModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MoveCommand implements Command {
    private final Logger logger = LogManager.getLogger(getClass());
    private final PlayerController playerController;
    private final PlayerModel player;
    private final int space;
    private final boolean directMovement;
    private final boolean goToJail;
    private final Type type;

    public enum Type {
        MOVE_OF,
        MOVE_TO
    }

    public MoveCommand(PlayerController playerController, PlayerModel player, Type type, int space, boolean directMovement, boolean goToJail) {
        this.playerController = playerController;
        this.player = player;
        this.type = type;
        this.space = space;
        this.directMovement = directMovement;
        this.goToJail = goToJail;
    }

    @Override
    public String getCommandName() {
        return "Move";
    }

    @Override
    public boolean isEnabled() {
        return playerController.getManager(player).canMove();
    }

    @Override
    public void execute() {
        if (player != null && playerController != null) {
            PlayerManager manager = playerController.getManager(player);
            if (goToJail) {
                //space = //TODO set jail space
            }
            if (Type.MOVE_TO.equals(type)) {
                logger.info("Executing movement to space {}", space);
                manager.moveTo(space, goToJail || directMovement);
            } else {
                logger.info("Executing movement of {} spaces", space);
                manager.move(space, goToJail || directMovement);
            }
        }
    }
}
