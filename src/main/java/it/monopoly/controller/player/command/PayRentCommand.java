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

public class PayRentCommand implements Command, BuyOrAuctionCallback {
    private final Logger logger = LogManager.getLogger(getClass());
    private final PropertyController propertyController;
    private final PlayerModel player;
    private final PropertyModel property;
    private final PayCommandBuilder payCommandBuilder;
    private final EventDispatcher eventDispatcher;
    private final TradeController tradeController;

    public PayRentCommand(
            PropertyController propertyController,
            PlayerModel player,
            PropertyModel property,
            PayCommandBuilder payCommandBuilder,
            EventDispatcher eventDispatcher,
            TradeController tradeController
    ) {
        this.propertyController = propertyController;
        this.player = player;
        this.property = property;
        this.payCommandBuilder = payCommandBuilder;
        this.eventDispatcher = eventDispatcher;
        this.tradeController = tradeController;
    }

    @Override
    public String getCommandName() {
        return "Pay Rent";
    }

    @Override
    public boolean isEnabled() {
        return propertyController.getManager(property).canCollectRent();
    }

    @Override
    public void execute() {
        PropertyManager propertyManager = propertyController.getManager(property);
        if (player == null) {
            return;
        }
        logger.info("Executing PayRentCommand for player {} on property {}", player.getId(), property.getName());
        if (propertyManager.getOwner() == null) {
            eventDispatcher.buyOrAuction(player, propertyManager.getReadable(), this);
        } else if (!player.equals(propertyManager.getOwner())) {
            payRent();
        }
    }

    private void payRent() {
        PropertyManager propertyManager = propertyController.getManager(property);
        payCommandBuilder
                .addDebtor(player)
                .addCreditor(propertyManager.getOwner())
                .setMoney(propertyManager.getPriceManager().getRent())
                .build()
                .execute();
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
