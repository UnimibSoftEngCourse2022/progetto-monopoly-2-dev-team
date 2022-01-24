package it.monopoly.controller.player.command;

import it.monopoly.controller.command.Command;
import it.monopoly.controller.property.PropertyController;
import it.monopoly.manager.property.PropertyManager;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PayRentCommand implements Command {
    private final Logger logger = LogManager.getLogger(getClass());
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
        logger.info("Executing PayRentCommand for player {} on property {}", player.getId(), property.getName());
        if (player.equals(propertyManager.getOwner())) {
            return;
        }
        payCommandBuilder
                .addDebtor(player)
                .addCreditor(propertyManager.getOwner())
                .setMoney(propertyManager.getPriceManager().getRent())
                .build()
                .execute();
    }
}
