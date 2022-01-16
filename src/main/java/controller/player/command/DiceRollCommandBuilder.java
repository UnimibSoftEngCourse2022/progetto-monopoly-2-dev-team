package controller.player.command;

import controller.command.Command;
import controller.command.CommandBuilder;
import controller.event.EventDispatcher;
import controller.player.PlayerController;
import model.player.PlayerModel;

public class DiceRollCommandBuilder implements CommandBuilder {
    private final PlayerController playerController;
    private final EventDispatcher eventDispatcher;
    private PlayerModel player;
    private final MoveCommandBuilder moveCommandBuilder;
    private Type type = Type.ROLL_FOR_MOVEMENT;

    public enum Type {
        ROLL_FOR_MOVEMENT,
        ROLL_TO_GET_OUT_OF_JAIL,
        ROLL_WITH_FIND
    }

    public DiceRollCommandBuilder(PlayerController playerController, EventDispatcher eventDispatcher, MoveCommandBuilder moveCommandBuilder) {
        this.playerController = playerController;
        this.eventDispatcher = eventDispatcher;
        this.moveCommandBuilder = moveCommandBuilder;
    }

    public void setPlayer(PlayerModel player) {
        this.player = player;
    }

    public void setType(Type type) {
        this.type = type;
    }


    @Override
    public Command build() {
        if (Type.ROLL_FOR_MOVEMENT.equals(type)) {
            return new DiceRollForMovementCommand(playerController, eventDispatcher, player, moveCommandBuilder);
        } else if (Type.ROLL_TO_GET_OUT_OF_JAIL.equals(type)) {

        } else {//if (Type.ROLL_WITH_FIND.equals(type))

        }
        return null;
    }
}
