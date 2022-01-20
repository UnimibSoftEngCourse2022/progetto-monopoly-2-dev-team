package controller.player.command;

import controller.command.Command;
import controller.property.PropertyController;
import manager.property.PropertyManager;
import model.player.PlayerModel;
import model.property.PropertyModel;

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
        if (player == null || propertyManager.getOwner() == null) {
            return;
        }
        if (player.equals(propertyManager.getOwner())) {
            return;
        }
        payCommandBuilder
                    .addDebtor(player)
                    .addCreditor(propertyManager.getOwner())
                    .setMoney(propertyManager.getPriceManager().getRent(property))
                    .build()
                    .execute();
    }
}
