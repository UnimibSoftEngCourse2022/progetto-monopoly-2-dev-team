package controller.player.command;

import controller.card.DrawableCardController;
import controller.command.Command;
import controller.player.PlayerController;
import manager.player.PlayerManager;
import model.DrawableCardModel;
import model.enums.DrawableCardCategory;
import model.player.PlayerModel;
import model.player.PlayerState;

public class GetOutOfJailCommand implements Command {

    DrawableCardController drawableCardController;
    PlayerController playerController;
    PlayerModel player;

    public GetOutOfJailCommand(PlayerController playerController, DrawableCardController drawableCardController, PlayerModel player) {
        this.playerController = playerController;
        this.drawableCardController = drawableCardController;
        this.player = player;
    }

    @Override
    public String getCommandName() {
        return "Get Out of Jail";
    }

    @Override
    public boolean isEnabled() {
        return playerController.getManager(player).getState().equals(PlayerState.IN_JAIL) && playerController.getManager(player).getDrawableCardModels().size() > 0;
    }

    @Override
    public void execute() {
        PlayerManager manager = playerController.getManager(player);
        if (isEnabled()) {
            if (player != null && playerController != null) {
                DrawableCardModel cardModel = manager.getDrawableCardModels().get(0);
                if (cardModel.getCategory().equals(DrawableCardCategory.CHANCE)) {
                    drawableCardController.getChances().add(cardModel);
                } else {
                    drawableCardController.getCommunities().add(cardModel);
                }
                manager.setState(PlayerState.FREE);
            }
        }
    }
}
