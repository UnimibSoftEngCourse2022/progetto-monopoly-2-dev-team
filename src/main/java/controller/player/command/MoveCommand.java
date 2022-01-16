package controller.player.command;

import controller.command.Command;
import controller.player.PlayerController;
import manager.player.PlayerManager;
import model.player.PlayerModel;

public class MoveCommand implements Command {
    private final PlayerController playerController;
    private final PlayerModel player;
    private final int space;
    private final boolean directMovement;

    public MoveCommand(PlayerController playerController, PlayerModel player, int space, boolean directMovement) {
        this.playerController = playerController;
        this.player = player;
        this.space = space;
        this.directMovement = directMovement;
    }

    @Override
    public String getCommandName() {
        return null;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void execute() {
        if (player != null && playerController != null) {
            PlayerManager manager = playerController.getManager(player);
            manager.moveTo(space, directMovement);
        }
    }
}
