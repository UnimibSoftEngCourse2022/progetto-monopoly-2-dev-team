package it.monopoly.controller.player.command;

import it.monopoly.controller.TradeController;
import it.monopoly.controller.command.Command;
import it.monopoly.controller.command.CommandBuilder;
import it.monopoly.controller.event.EventDispatcher;
import it.monopoly.controller.property.PropertyController;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyModel;

public class BuyOrAuctionCommandBuilder implements CommandBuilder {
    private final PropertyController propertyController;
    private final TradeController tradeController;
    private final EventDispatcher eventDispatcher;
    private PropertyModel property;
    private PlayerModel player;

    public BuyOrAuctionCommandBuilder(PropertyController propertyController,
                                      TradeController tradeController,
                                      EventDispatcher eventDispatcher) {
        this.propertyController = propertyController;
        this.tradeController = tradeController;
        this.eventDispatcher = eventDispatcher;
    }

    public BuyOrAuctionCommandBuilder setProperty(PropertyModel property) {
        this.property = property;
        return this;
    }

    public BuyOrAuctionCommandBuilder setPlayer(PlayerModel player) {
        this.player = player;
        return this;
    }

    @Override
    public Command build() {
        return new BuyOrAuctionCommand(propertyController,
                property,
                player,
                tradeController,
                eventDispatcher);
    }
}
