package controller.player.command;

import controller.command.Command;
import controller.player.PlayerController;
import manager.loyaltyprogram.LoyaltyPercentage;
import manager.loyaltyprogram.LoyaltyPoints;
import manager.loyaltyprogram.LoyaltyProgram;
import manager.player.PlayerManager;
import model.player.PlayerModel;

public class JoinLoyaltyProgramCommand implements Command {

    private final PlayerController playerController;
    private final PlayerModel player;
    private final PlayerModel creditor;
    private final LoyaltyProgram.Type type;


    public JoinLoyaltyProgramCommand(PlayerController playerController,
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
        return "Join Loyalty Program";
    }

    @Override
    public boolean isEnabled() {
        return playerController.getManager(player).getLoyaltyProgram() == null;
    }

    @Override
    public void execute() {
        if (player != null && playerController != null && isEnabled()) {
            PlayerManager playerManager = playerController.getManager(player);
            if (LoyaltyProgram.Type.PERCENTAGE.equals(type)) {
                playerManager.joinLoyaltyProgram(new LoyaltyPercentage(creditor));
            } else {
                playerManager.joinLoyaltyProgram(new LoyaltyPoints(creditor));
            }
        }
    }
}
