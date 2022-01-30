package it.monopoly.controller.player.command;

import it.monopoly.controller.command.Command;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.manager.loyaltyprogram.LoyaltyPercentage;
import it.monopoly.manager.loyaltyprogram.LoyaltyPoints;
import it.monopoly.manager.loyaltyprogram.LoyaltyProgram;
import it.monopoly.manager.player.PlayerManager;
import it.monopoly.model.player.PlayerModel;

public class QuitLoyaltyProgramCommand implements Command {

    private final PlayerController playerController;
    private final PlayerModel player;
    private final PlayerModel creditor;
    private final LoyaltyProgram.Type type;


    public QuitLoyaltyProgramCommand(PlayerController playerController,
                                     PlayerModel player,
                                     PlayerModel creditor,
                                     LoyaltyProgram.Type type) {
        this.playerController = playerController;
        this.player = player;
        this.creditor = creditor;
        this.type = type;
    }

    @Override
    public String getCommandName() {
        return "Quit Loyalty Program";
    }

    @Override
    public boolean isEnabled() {
        PlayerManager manager = playerController.getManager(player);
        return manager.getLoyaltyProgram() != null && creditor.equals(manager.getLoyaltyProgram().getCreditor());
    }

    @Override
    public void execute() {
        if (player != null && playerController != null && isEnabled()) {
            PlayerManager playerManager = playerController.getManager(player);
            playerManager.quitLoyaltyProgram();
        }
    }
}
