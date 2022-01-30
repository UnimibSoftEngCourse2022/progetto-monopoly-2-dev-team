package it.monopoly.controller.player.command;

import it.monopoly.controller.command.Command;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.manager.loyaltyprogram.LoyaltyPercentage;
import it.monopoly.manager.loyaltyprogram.LoyaltyPoints;
import it.monopoly.manager.loyaltyprogram.LoyaltyProgram;
import it.monopoly.manager.player.PlayerManager;
import it.monopoly.model.player.PlayerModel;

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
        PlayerManager manager = playerController.getManager(player);
        String name = "Join Loyalty Program";
        if (manager.canJoinLoyaltyInTurns() != 0) {
            name += " (in " + manager.canJoinLoyaltyInTurns() + " turns)";
        }
        return name;
    }

    @Override
    public boolean isEnabled() {
        PlayerManager manager = playerController.getManager(player);
        return manager.canJoinLoyalty();
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
