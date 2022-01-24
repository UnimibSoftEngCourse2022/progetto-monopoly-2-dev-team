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
    private int space;
    private int[] nearSpaces;
    private final boolean directMovement;
    private final boolean goToJail;
    private final Type type;

    public enum Type {
        MOVE_OF,
        MOVE_TO,
        MOVE_NEAR
    }

    public MoveCommand(PlayerController playerController, PlayerModel player, Type type, int space, int[] nearSpaces, boolean directMovement, boolean goToJail) {
        this.playerController = playerController;
        this.player = player;
        this.type = type;
        this.space = space;
        this.nearSpaces = nearSpaces;
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
            if(goToJail) {
                space = 10;
            }
            if(Type.MOVE_TO.equals(type) || goToJail) {
                logger.info("Executing movement to space {}", space);
                manager.moveTo(space, goToJail || directMovement);
            } else if (Type.MOVE_OF.equals(type)){
                logger.info("Executing movement of {} spaces", space);
                manager.move(space, directMovement);
            } else {
                int index = 0;
                int min = 40;
                for (int i = 0; i < nearSpaces.length; i++) {
                    if (nearSpaces[i] - manager.getPosition().getIntPosition() > 0
                            && nearSpaces[i] - manager.getPosition().getIntPosition() < min) {
                        index = i;
                        min = nearSpaces[i] - manager.getPosition().getIntPosition();
                    }
                }
                manager.moveTo(nearSpaces[index],false);
            }
            if (goToJail) {
                manager.goToJail();
                manager.move(space, goToJail || directMovement);
            }
        }
    }
}
