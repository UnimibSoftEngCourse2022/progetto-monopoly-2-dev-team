package it.monopoly.controller.board.space;

import it.monopoly.controller.command.CommandBuilderDispatcher;
import it.monopoly.controller.player.command.BuyOrAuctionCommandBuilder;
import it.monopoly.controller.player.command.PayRentCommandBuilder;
import it.monopoly.manager.property.PropertyManager;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyModel;

public class PropertySpace extends AbstractSpace {

    private final PropertyManager manager;

    public PropertySpace(CommandBuilderDispatcher commandBuilderDispatcher, PropertyManager manager) {
        super(commandBuilderDispatcher);
        this.manager = manager;
    }

    public PropertyModel getProperty() {
        return manager.getModel();
    }

    @Override
    public void applyEffect(PlayerModel player) {
        if (manager.getOwner() == null) {
            commandBuilderDispatcher.createCommandBuilder(BuyOrAuctionCommandBuilder.class)
                    .setPlayer(player)
                    .setProperty(manager.getModel())
                    .build()
                    .execute();
        } else {
            commandBuilderDispatcher.createCommandBuilder(PayRentCommandBuilder.class)
                    .setProperty(manager.getModel())
                    .setPlayer(player)
                    .build()
                    .execute();
        }
    }
}
