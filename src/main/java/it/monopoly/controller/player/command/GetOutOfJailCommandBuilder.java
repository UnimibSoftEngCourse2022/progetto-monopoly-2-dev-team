package it.monopoly.controller.player.command;

import it.monopoly.controller.board.card.DrawableCardController;
import it.monopoly.controller.command.Command;
import it.monopoly.controller.command.CommandBuilder;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.player.PlayerState;

public class GetOutOfJailCommandBuilder implements CommandBuilder {

    private final DrawableCardController drawableCardController;
    private final PlayerController playerController;
    private PlayerModel player;

    public GetOutOfJailCommandBuilder(
            PlayerController playerController,
            DrawableCardController drawableCardController
    ) {
        this.playerController = playerController;
        this.drawableCardController = drawableCardController;
    }

    public GetOutOfJailCommandBuilder setPlayer(PlayerModel player) {
        this.player = player;
        return this;
    }

    @Override
    public Command build() {
        if (!PlayerState.IN_JAIL.equals(playerController.getManager(player).getState())) {
            return null;
        }
        return new GetOutOfJailCommand(
                playerController,
                drawableCardController,
                player
        );
    }
}
