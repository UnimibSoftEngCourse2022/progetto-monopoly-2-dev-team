package it.monopoly.controller.board.space;

import it.monopoly.controller.command.CommandBuilderDispatcher;
import it.monopoly.controller.player.command.PayCommandBuilder;
import it.monopoly.model.player.PlayerModel;

public class TaxSpace extends AbstractSpace {

    private final int tax;

    public TaxSpace(CommandBuilderDispatcher commandBuilderDispatcher, int tax) {
        super(commandBuilderDispatcher);
        this.tax = tax;
    }

    @Override
    public void applyEffect(PlayerModel player) {
        commandBuilderDispatcher
                .createCommandBuilder(PayCommandBuilder.class)
                .setMoney(tax)
                .addDebtor(player)
                .build()
                .execute();
    }
}
