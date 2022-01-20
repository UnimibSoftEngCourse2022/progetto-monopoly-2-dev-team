package controller.board.space;

import controller.command.Command;
import controller.command.CommandBuilderDispatcher;
import controller.player.PlayerController;
import controller.player.command.MoveCommand;
import controller.player.command.MoveCommandBuilder;
import model.player.PlayerModel;

public class GoToJailSpace extends AbstractSpace {

    private PlayerController playerController;

    public GoToJailSpace(CommandBuilderDispatcher commandBuilderDispatcher,
                         PlayerController playerController) {
        super(commandBuilderDispatcher);
        this.playerController = playerController;
    }

    @Override
    public void applyEffect(PlayerModel player) {
        commandBuilderDispatcher
                .createCommandBuilder(MoveCommandBuilder.class)
                .setPlayer(player)
                .setGoToJail(true)
                .build()
                .execute();
        playerController.getManager(player).goToJail();
    }
}
