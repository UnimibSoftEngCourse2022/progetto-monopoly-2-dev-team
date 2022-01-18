package controller.board.space;

import controller.command.CommandBuilderDispatcher;
import controller.card.DrawableCardController;
import controller.player.command.MoveCommand;
import controller.player.command.MoveCommandBuilder;
import model.DrawableCardModel;
import model.enums.Movement;
import model.player.PlayerModel;

import static model.enums.Movement.*;

public class CommunityChestSpace extends AbstractSpace {

    private DrawableCardController drawableCardController;

    public CommunityChestSpace(CommandBuilderDispatcher commandBuilderDispatcher, DrawableCardController drawableCardController) {
        super(commandBuilderDispatcher);
        this.drawableCardController = drawableCardController;
    }

    @Override
    public void applyEffect(PlayerModel player) {

        DrawableCardModel card = drawableCardController.draw(DrawableCardController.Type.COMMUNITY_CHEST);
        Movement movement = card.getMovement();
        MoveCommandBuilder commandBuilder = null;

        if (card != null) {
            if (movement != null) {
                int where = card.getWhere();
                boolean direct = card.isDirect();

                commandBuilder = commandBuilderDispatcher
                        .createCommandBuilder(MoveCommandBuilder.class)
                        .setPlayer(player)
                        .setDirectMovement(direct);

                if (MOVE_OF.equals(movement)) {
                    commandBuilder.setSpace(MoveCommand.Type.MOVE_OF, where);
                } else if (MOVE_TO.equals(movement)) {
                    commandBuilder.setSpace(MoveCommand.Type.MOVE_TO, where);
                } else {
                    // TODO: commandBuilder.setSpace(MoveCommand.Type.MOVE_NEAR, where);
                }

            } else { // payment command
                // TODO: payCommandBuilder
            }
        }

        if (commandBuilder != null) {
            commandBuilder
                    .build()
                    .execute();
        }
    }
}
