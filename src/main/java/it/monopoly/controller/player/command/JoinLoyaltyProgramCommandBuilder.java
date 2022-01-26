package it.monopoly.controller.player.command;

import it.monopoly.controller.command.CommandBuilder;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.manager.loyaltyprogram.LoyaltyProgram;
import it.monopoly.model.player.PlayerModel;

public class JoinLoyaltyProgramCommandBuilder implements CommandBuilder {

    private final PlayerController playerController;
    private PlayerModel debtor;
    private PlayerModel creditor;
    private LoyaltyProgram.Type type;

    public JoinLoyaltyProgramCommandBuilder(PlayerController playerController) {
        this.playerController = playerController;
    }

    public JoinLoyaltyProgramCommandBuilder setDebtor(PlayerModel player) {
        this.debtor = player;
        return this;
    }

    public JoinLoyaltyProgramCommandBuilder setCreditor(PlayerModel creditor) {
        this.creditor = creditor;
        return this;
    }

    public JoinLoyaltyProgramCommandBuilder setType(LoyaltyProgram.Type type) {
        this.type = type;
        return this;
    }

    @Override
    public JoinLoyaltyProgramCommand build() {
        return new JoinLoyaltyProgramCommand(playerController,
                debtor,
                creditor,
                type);
    }
}
