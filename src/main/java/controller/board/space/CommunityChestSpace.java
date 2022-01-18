package controller.board.space;

import controller.command.CommandBuilderDispatcher;
import model.player.PlayerModel;

public class CommunityChestSpace extends AbstractSpace {

    public CommunityChestSpace(CommandBuilderDispatcher commandBuilderDispatcher) {
        super(commandBuilderDispatcher);
    }

    @Override
    public void applyEffect(PlayerModel player) {

    }
}
