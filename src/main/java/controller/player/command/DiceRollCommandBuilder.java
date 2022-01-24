package controller.player.command;

import controller.command.Command;
import controller.command.CommandBuilder;
import controller.command.ComposableCommand;
import controller.event.EventDispatcher;
import controller.player.PlayerController;
import manager.player.PlayerManager;
import model.player.PlayerModel;
import model.player.PlayerState;

public class DiceRollCommandBuilder implements CommandBuilder {
    private final PlayerController playerController;
    private final EventDispatcher eventDispatcher;
    private PlayerModel player;
    private final MoveCommandBuilder moveCommandBuilder;
    private final PayCommandBuilder payCommandBuilder;

    public DiceRollCommandBuilder(PlayerController playerController,
                                  EventDispatcher eventDispatcher,
                                  MoveCommandBuilder moveCommandBuilder,
                                  PayCommandBuilder payCommandBuilder) {
        this.playerController = playerController;
        this.eventDispatcher = eventDispatcher;
        this.moveCommandBuilder = moveCommandBuilder;
        this.payCommandBuilder = payCommandBuilder;
    }

    public DiceRollCommandBuilder setPlayer(PlayerModel player) {
        this.player = player;
        return this;
    }

    @Override
    public Command build() {
        PlayerManager manager = playerController.getManager(player);
        if (PlayerState.IN_JAIL.equals(manager.getState())) {
            return new DiceRollForJailCommand(playerController, eventDispatcher, player, moveCommandBuilder, payCommandBuilder);
        }

        DiceRollForMovementCommand rollForMovementCommand = new DiceRollForMovementCommand(playerController, eventDispatcher, player, moveCommandBuilder);

        if (PlayerState.FREE.equals(manager.getState())) {
            return rollForMovementCommand;
        } else if (PlayerState.FINED.equals(manager.getState())) {

            PayCommand payCommand = payCommandBuilder.addDebtor(player).setMoney(50).build();

            ComposableCommand composableCommand = new ComposableCommand() {
                @Override
                public String getCommandName() {
                    return rollForMovementCommand.getCommandName();
                }

                @Override
                public boolean isEnabled() {
                    return rollForMovementCommand.isEnabled();
                }
            };
            composableCommand.addCommand(rollForMovementCommand);
            composableCommand.addCommand(payCommand);
            return composableCommand;
        }
        return null;
    }
}