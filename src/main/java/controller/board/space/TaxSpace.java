package controller.board.space;

import controller.command.CommandBuilderDispatcher;
import controller.player.command.PayCommandBuilder;
import model.player.PlayerModel;

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
