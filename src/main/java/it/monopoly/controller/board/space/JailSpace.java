package it.monopoly.controller.board.space;

import it.monopoly.controller.command.CommandBuilderDispatcher;
import it.monopoly.controller.event.EventDispatcher;
import it.monopoly.controller.event.callback.FirstSecondChoice;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.model.player.PlayerModel;

public class JailSpace extends AbstractSpace {
    private final PlayerController playerController;
    private final EventDispatcher eventDispatcher;

    public JailSpace(PlayerController playerController,
                     CommandBuilderDispatcher commandBuilderDispatcher,
                     EventDispatcher eventDispatcher) {
        super(commandBuilderDispatcher);
        this.playerController = playerController;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void applyEffect(PlayerModel player) {
        eventDispatcher.jailOrFine(player, choice -> {
            if (FirstSecondChoice.SECOND.equals(choice)) {
                playerController.getManager(player).getOutOfJailWithFine();
            }
        });
    }
}
