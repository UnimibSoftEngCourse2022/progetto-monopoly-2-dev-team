package controller.board.space;

import controller.card.DrawableCardController;
import controller.command.CommandBuilder;
import controller.command.CommandBuilderDispatcher;
import controller.player.PlayerController;
import controller.player.command.MoveCommand;
import controller.player.command.MoveCommandBuilder;
import controller.player.command.PayCommandBuilder;
import model.DrawableCardModel;
import model.enums.CreditorDebtor;
import model.enums.Movement;
import model.enums.Pay;
import model.player.PlayerModel;
import model.property.PropertyModel;

import java.util.List;

import static model.enums.Movement.MOVE_OF;
import static model.enums.Movement.MOVE_TO;

public class CommunityChestSpace extends DrawableCardSpace {

    public CommunityChestSpace(CommandBuilderDispatcher commandBuilderDispatcher, DrawableCardController drawableCardController, PlayerController playerController) {
        super(commandBuilderDispatcher, drawableCardController, playerController);
    }

    @Override
    protected DrawableCardModel draw() {
        return drawableCardController.draw(DrawableCardController.Type.COMMUNITY_CHEST);
    }
}
