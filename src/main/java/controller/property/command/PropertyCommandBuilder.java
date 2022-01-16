package controller.property.command;

import controller.TradeController;
import controller.command.Command;
import controller.command.CommandBuilder;
import controller.command.ComposableCommand;
import controller.player.command.PayCommandBuilder;
import controller.property.PropertyController;
import manager.property.PropertyManager;
import model.property.PropertyModel;

public class PropertyCommandBuilder implements CommandBuilder {
    private final PropertyController propertyController;
    private final TradeController tradeController;
    private PropertyModel property;
    private Type type;

    public enum Type {
        BUILD_HOUSE,
        SELL_HOUSE,
        BUILD_HOTEL,
        SELL_HOTEL,
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

        if (Type.BUILD_HOTEL.equals(type)) {
            propertyCommand = new BuildHotelCommand(propertyController, property);
            price = propertyManager.getPriceManager().getHotelPrice(property);
        } else if (Type.BUILD_HOUSE.equals(type)) {
            propertyCommand = new BuildHouseCommand(propertyController, property);
            price = propertyManager.getPriceManager().getHousePrice(property);
        } else if (Type.SELL_HOTEL.equals(type)) {
            propertyCommand = new SellHotelCommand(propertyController, property);
            price = propertyManager.getPriceManager().getHotelPrice(property) / 2;
        } else if (Type.SELL_HOUSE.equals(type)) {
            propertyCommand = new SellHouseCommand(propertyController, property);
            price = propertyManager.getPriceManager().getHotelPrice(property) / 2;
        } else {//(Type.MORTGAGE.equals(type))
            return new MortgageCommand(propertyController, property);
        }

        Command transactionCommand = new PayCommandBuilder(tradeController)
                .addDebtor(propertyManager.getOwner())
                .setMoney(price)
                .build();

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
