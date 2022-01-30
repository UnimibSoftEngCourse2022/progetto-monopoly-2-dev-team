package it.monopoly.controller.property.command;

import it.monopoly.controller.TradeController;
import it.monopoly.controller.command.Command;
import it.monopoly.controller.event.EventDispatcher;
import it.monopoly.controller.property.PropertyController;
import it.monopoly.manager.SellManager;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyModel;

public class SellPropertyCommand implements Command {
    private final PropertyController propertyController;
    private final PropertyModel property;
    private final TradeController tradeController;
    private final EventDispatcher eventDispatcher;

    public SellPropertyCommand(PropertyController propertyController,
                               PropertyModel property,
                               TradeController tradeController,
                               EventDispatcher eventDispatcher) {
        this.propertyController = propertyController;
        this.property = property;
        this.tradeController = tradeController;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public String getCommandName() {
        return "Sell Property";
    }

    @Override
    public boolean isEnabled() {
        return propertyController.getManager(property).canSell();
    }

    @Override
    public void execute() {
        PlayerModel owner = propertyController.getManager(property).getOwner();
        eventDispatcher.startOffer(new SellManager(owner, property, tradeController));
    }
}
