package controller.player.command;

import controller.command.CommandBuilder;
import controller.player.PlayerController;
import model.player.PlayerModel;

public class MoveCommandBuilder implements CommandBuilder {
    private final PlayerController playerController;
    private PlayerModel player;
    private int space;
    private boolean directMovement = false;

    public MoveCommandBuilder(PlayerController playerController) {
        this.playerController = playerController;
    }

    public MoveCommandBuilder setPlayer(PlayerModel player) {
        this.player = player;
        return this;
    }

    public MoveCommandBuilder setSpace(int space) {
        this.space = space;
        return this;
    }

    public MoveCommandBuilder setDirectMovement(boolean directMovement) {
        this.directMovement = directMovement;
        return this;
    }

    public MoveCommand build() {
        return new MoveCommand(
                playerController,
                player,
                space,
                directMovement
        );
    }
}
