package it.monopoly.controller.player.command;

import it.monopoly.controller.command.CommandBuilder;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.model.player.PlayerModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MoveCommandBuilder implements CommandBuilder {
    private final Logger logger = LogManager.getLogger(getClass());
    private final PlayerController playerController;
    private PlayerModel player;
    private int space;
    private boolean directMovement = false;
    private boolean goToJail = false;
    private MoveCommand.Type type;

    public MoveCommandBuilder(PlayerController playerController) {
        this.playerController = playerController;
    }

    public MoveCommandBuilder setPlayer(PlayerModel player) {
        this.player = player;
        return this;
    }

    public MoveCommandBuilder setSpace(MoveCommand.Type type, int space) {
        this.type = type;
        this.space = space;
        return this;
    }

    public MoveCommandBuilder setDirectMovement(boolean directMovement) {
        this.directMovement = directMovement;
        return this;
    }

    public MoveCommandBuilder setGoToJail(boolean goToJail) {
        this.goToJail = goToJail;
        return this;
    }

    public MoveCommand build() {
        logger.info("Building movement command for player {}", player.getId());
        return new MoveCommand(
                playerController,
                player,
                type,
                space,
                directMovement,
                goToJail
        );
    }
}
