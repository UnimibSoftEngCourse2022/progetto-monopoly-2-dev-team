package controller.player.command;

import controller.command.Command;
import controller.player.PlayerController;
import controller.property.PropertyController;
import manager.property.PropertyManager;
import model.player.PlayerModel;
import model.property.PropertyModel;

public class PayRentCommand implements Command {
    private PropertyController propertyController;
    private PlayerModel player;
    private PropertyModel property;
    private int money;
    private PayCommandBuilder payCommandBuilder;

    public PayRentCommand(
            PropertyController propertyController,
            PlayerModel player,
            PropertyModel property,
            int money,
            PayCommandBuilder payCommandBuilder
    ) {
        this.propertyController = propertyController;
        this.player = player;
        this.property = property;
        this.money = money;
        this.payCommandBuilder = payCommandBuilder;
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
        payCommandBuilder
                .addDebtor(player)
                .setMoney(propertyManager.getPriceManager().getRent(property))
                .build()
                .execute();
    }
}
