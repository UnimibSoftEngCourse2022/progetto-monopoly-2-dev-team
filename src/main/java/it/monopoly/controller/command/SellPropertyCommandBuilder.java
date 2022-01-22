package it.monopoly.controller.command;

import it.monopoly.controller.TradeController;
import it.monopoly.controller.event.EventDispatcher;
import it.monopoly.controller.property.PropertyController;
import it.monopoly.controller.property.command.SellPropertyCommand;
import it.monopoly.model.property.PropertyModel;

public class SellPropertyCommandBuilder implements CommandBuilder {
    private final PropertyController propertyController;
    private final TradeController tradeController;
    private final EventDispatcher eventDispatcher;
    private PropertyModel property;

    public SellPropertyCommandBuilder(PropertyController propertyController,
                                      TradeController tradeController,
                                      EventDispatcher eventDispatcher) {
        this.propertyController = propertyController;
        this.tradeController = tradeController;
        this.eventDispatcher = eventDispatcher;
    }

    public SellPropertyCommandBuilder setProperty(PropertyModel property) {
        this.property = property;
        return this;
    }

    @Override
    public Command build() {
        return new SellPropertyCommand(propertyController, property, tradeController, eventDispatcher);
    }
}
