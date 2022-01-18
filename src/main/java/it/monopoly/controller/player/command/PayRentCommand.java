package it.monopoly.controller.player.command;

import it.monopoly.controller.command.Command;
import it.monopoly.controller.property.PropertyController;
import it.monopoly.manager.property.PropertyManager;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyModel;

public class PayRentCommand implements Command {
    private final PropertyController propertyController;
    private final PlayerModel player;
    private final PropertyModel property;
    private final PayCommandBuilder payCommandBuilder;

    public PayRentCommand(
            PropertyController propertyController,
            PlayerModel player,
            PropertyModel property,
            PayCommandBuilder payCommandBuilder
    ) {
        this.propertyController = propertyController;
        this.player = player;
        this.property = property;
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
