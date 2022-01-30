package it.monopoly.controller.board.space;

import it.monopoly.controller.command.CommandBuilderDispatcher;
import it.monopoly.controller.player.command.PayCommandBuilder;
import it.monopoly.manager.randomizer.RandomizationManager;
import it.monopoly.model.player.PlayerModel;

public class TaxSpace extends AbstractSpace {

    private final RandomizationManager randomizationManager;
    private final int tax;

    public TaxSpace(
            CommandBuilderDispatcher commandBuilderDispatcher,
            RandomizationManager randomizationManager,
            int tax
    ) {
        super(commandBuilderDispatcher);
        this.randomizationManager = randomizationManager;
        this.tax = tax;
    }

    @Override
    public void applyEffect(PlayerModel player) {
        float price = tax;
        if (randomizationManager != null) {
            price *= randomizationManager.getTaxRandomizerManager().getTaxesPercentage();
        }
        commandBuilderDispatcher
                .createCommandBuilder(PayCommandBuilder.class)
                .setMoney((int) price)
                .addDebtor(player)
                .build()
                .execute();
    }
}
