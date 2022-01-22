package it.monopoly.controller.property.command;

import it.monopoly.controller.TradeController;
import it.monopoly.controller.command.Command;
import it.monopoly.controller.command.CommandBuilder;
import it.monopoly.controller.command.ComposableCommand;
import it.monopoly.controller.player.command.PayCommand;
import it.monopoly.controller.player.command.PayCommandBuilder;
import it.monopoly.controller.property.PropertyController;
import it.monopoly.manager.property.PropertyManager;
import it.monopoly.model.property.PropertyModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PropertyCommandBuilder implements CommandBuilder {
    private final Logger logger = LogManager.getLogger(getClass());
    private final PropertyController propertyController;
    private final TradeController tradeController;
    private PropertyModel property;
    private Type type;

    public enum Type {
        BUILD,
        SELL,
        MORTGAGE
    }

    public PropertyCommandBuilder(PropertyController propertyController, TradeController tradeController) {
        this.propertyController = propertyController;
        this.tradeController = tradeController;
    }

    public PropertyCommandBuilder setProperty(PropertyModel property) {
        this.property = property;
        return this;
    }

    public PropertyCommandBuilder setType(Type type) {
        this.type = type;
        return this;
    }

    public Command build() {
        if (property == null) {
            return null;
        }

        PropertyManager propertyManager = propertyController.getManager(property);
        Command propertyCommand;
        int price;

        logger.debug("Building {} property command for property {}", type, property.getName());

        if (Type.BUILD.equals(type)) {
            if (property.getHouseNumber() < PropertyManager.MAX_NUMBER_OF_HOUSES) {
                propertyCommand = new BuildHouseCommand(propertyController, property);
                price = propertyManager.getPriceManager().getHousePrice(property);
            } else {
                propertyCommand = new BuildHotelCommand(propertyController, property);
                price = propertyManager.getPriceManager().getHotelPrice(property);
            }
        } else if (Type.SELL.equals(type)) {
            if (property.getHotelNumber() > 0) {
                propertyCommand = new SellHotelCommand(propertyController, property);
                price = propertyManager.getPriceManager().getHotelPrice(property) / 2;
            } else {
                propertyCommand = new SellHouseCommand(propertyController, property);
                price = propertyManager.getPriceManager().getHousePrice(property) / 2;
            }
        } else {//(Type.MORTGAGE.equals(type))
            propertyCommand = new MortgageCommand(propertyController, property);
            price = propertyManager.getPriceManager().getMortgageValue(property);
        }

        PayCommandBuilder payCommandBuilder = new PayCommandBuilder(tradeController);
        if (Type.BUILD.equals(type)) {
            payCommandBuilder.addDebtor(propertyManager.getOwner());
        } else {
            payCommandBuilder.addCreditor(propertyManager.getOwner());
        }

        PayCommand transactionCommand = payCommandBuilder.setMoney(price).build();

        ComposableCommand command = new ComposableCommand() {
            @Override
            public String getCommandName() {
                return propertyCommand.getCommandName();
            }

            @Override
            public boolean isEnabled() {
                return propertyCommand.isEnabled();
            }
        };

        command.addCommand(propertyCommand);
        command.addCommand(transactionCommand);

        return command;
    }
}
