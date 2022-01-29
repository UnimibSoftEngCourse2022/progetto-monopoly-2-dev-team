package it.monopoly.controller.player.command;

import it.monopoly.controller.board.card.DrawableCardController;
import it.monopoly.controller.command.Command;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.manager.player.PlayerManager;
import it.monopoly.model.DrawableCardModel;
import it.monopoly.model.enums.DrawableCardCategory;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.player.PlayerState;

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
        PlayerManager manager = playerController.getManager(player);
        return manager.getState().equals(PlayerState.IN_JAIL) && !manager.getDrawableCardModels().isEmpty();
    }

    @Override
    public void execute() {
        PlayerManager manager = playerController.getManager(player);
        if (isEnabled() && player != null && playerController != null) {
            DrawableCardModel cardModel = manager.getDrawableCardModels().get(0);
            if (cardModel.getCategory().equals(DrawableCardCategory.CHANCE)) {
                drawableCardController.getChancesCards().add(cardModel);
            } else {
                drawableCardController.getCommunitiesCards().add(cardModel);
            }
            manager.getDrawableCardModels().remove(cardModel);
            manager.getOutOfJail();
        }
    }

}
