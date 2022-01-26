package it.monopoly.controller.player.command;

import it.monopoly.controller.TradeController;
import it.monopoly.controller.command.Command;
import it.monopoly.controller.event.EventDispatcher;
import it.monopoly.controller.event.callback.BuyOrAuctionCallback;
import it.monopoly.controller.property.PropertyController;
import it.monopoly.manager.AuctionManager;
import it.monopoly.manager.property.PropertyManager;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BuyOrAuctionCommand implements Command, BuyOrAuctionCallback {
    private final Logger logger = LogManager.getLogger(getClass());
    private final PropertyController propertyController;
    private final TradeController tradeController;
    private final EventDispatcher eventDispatcher;
    private final PropertyModel property;
    private final PlayerModel player;

    public BuyOrAuctionCommand(PropertyController propertyController,
                               PropertyModel property,
                               PlayerModel player,
                               TradeController tradeController,
                               EventDispatcher eventDispatcher) {
        this.propertyController = propertyController;
        this.property = property;
        this.player = player;
        this.tradeController = tradeController;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public String getCommandName() {
        return "Rent or Auction";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void execute() {
        PropertyManager propertyManager = propertyController.getManager(property);
        if (player == null) {
            return;
        }
        logger.info("Executing BuyOrRentCommand for player {} on property {}", player.getId(), property.getName());
        if (propertyManager.getOwner() == null) {
            eventDispatcher.buyOrAuction(player, propertyManager.getReadable(), this);
        }
    }

    @Override
    public void buy() {
        tradeController.buyProperty(player, property);
    }

    @Override
    public void startAuction() {
        eventDispatcher.startOffer(new AuctionManager(player, property, tradeController));
    }
}
