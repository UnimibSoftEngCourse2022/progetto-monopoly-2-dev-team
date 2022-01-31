package it.monopoly.controller.player.command;

import it.monopoly.controller.command.Command;
import it.monopoly.controller.command.CommandBuilder;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.manager.loyaltyprogram.LoyaltyProgram;
import it.monopoly.model.player.PlayerModel;

public class LoyaltyProgramCommandBuilder implements CommandBuilder {

    private final PlayerController playerController;
    private PlayerModel debtor;
    private PlayerModel creditor;
    private LoyaltyProgram.Type type;

    public LoyaltyProgramCommandBuilder(PlayerController playerController) {
        this.playerController = playerController;
    }

    public LoyaltyProgramCommandBuilder setDebtor(PlayerModel player) {
        this.debtor = player;
        return this;
    }

    public LoyaltyProgramCommandBuilder setCreditor(PlayerModel creditor) {
        this.creditor = creditor;
        return this;
    }

    public LoyaltyProgramCommandBuilder setType(LoyaltyProgram.Type type) {
        this.type = type;
        return this;
    }

    @Override
    public Command build() {
        if (creditor.equals(debtor)) {
            return null;
        }
        LoyaltyProgram loyaltyProgram = playerController.getManager(debtor).getLoyaltyProgram();
        if (loyaltyProgram != null && loyaltyProgram.getCreditor().equals(creditor)) {
            return new QuitLoyaltyProgramCommand(playerController,
                    debtor,
                    creditor
            );
        }
        return new JoinLoyaltyProgramCommand(playerController,
                debtor,
                creditor,
                type);
    }
}
