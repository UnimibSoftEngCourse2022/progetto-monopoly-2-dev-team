package controller.player.command;

import controller.command.Command;
import controller.player.PlayerController;
import manager.player.PlayerManager;
import model.player.PlayerModel;

public class MoveCommand implements Command {
    private final PlayerController playerController;
    private final PlayerModel player;
    private int space;
    private final boolean directMovement;
    private final boolean goToJail;
    private final Type type;

    public enum Type {
        MOVE_OF,
        MOVE_TO,
        MOVE_NEAR
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
            if(goToJail) {
                space = 10;
            }
            if(Type.MOVE_TO.equals(type) || goToJail) {
                manager.moveTo(space, goToJail || directMovement);
            } else if (Type.MOVE_OF.equals(type)){
                manager.move(space, directMovement);
            } else {
                // TODO: MOVE_NEAR
            }
            if (goToJail) {
                manager.goToJail();
            }
        }
    }
}
