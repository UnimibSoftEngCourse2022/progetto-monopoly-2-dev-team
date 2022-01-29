package it.monopoly.controller.player.command;

import it.monopoly.controller.command.Command;
import it.monopoly.controller.event.EventDispatcher;
import it.monopoly.controller.event.callback.FirstOrSecondCallback;
import it.monopoly.controller.event.callback.FirstSecondChoice;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.controller.property.PropertyController;
import it.monopoly.manager.loyaltyprogram.LoyaltyProgram;
import it.monopoly.manager.property.PropertyManager;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PayRentCommand implements Command {
    private final Logger logger = LogManager.getLogger(getClass());
    private final PropertyController propertyController;
    private final PlayerController playerController;
    private final PlayerModel player;
    private final PropertyModel property;
    private final PayCommandBuilder payCommandBuilder;
    private final EventDispatcher eventDispatcher;

    public PayRentCommand(
            PropertyController propertyController,
            PlayerController playerController,
            PlayerModel player,
            PropertyModel property,
            PayCommandBuilder payCommandBuilder,
            EventDispatcher eventDispatcher
    ) {
        this.propertyController = propertyController;
        this.playerController = playerController;
        this.player = player;
        this.property = property;
        this.payCommandBuilder = payCommandBuilder;
        this.eventDispatcher = eventDispatcher;
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
        if (!isEnabled()) return;
        logger.info("Executing PayRentCommand for player {} on property {}", player.getId(), property.getName());
        PropertyManager propertyManager = propertyController.getManager(property);
        if (!player.equals(propertyManager.getOwner())) {
            payRent();
        }
    }

    private void payRent() {
        PropertyManager propertyManager = propertyController.getManager(property);
        LoyaltyProgram loyaltyProgram = playerController.getManager(player).getLoyaltyProgram();

        int rent = propertyManager.getPriceManager().getRent();

        if (loyaltyProgram != null && propertyManager.getOwner().equals(loyaltyProgram.getCreditor())) {
            if (loyaltyProgram.getType().equals(LoyaltyProgram.Type.PERCENTAGE)) {
                int price = loyaltyProgram.spendSales(propertyManager.getOwner(), rent);
                loyaltyProgram.gatherSales(propertyManager.getOwner(), rent);
                pay(price);
            } else {
                eventDispatcher.useLoyaltyPoints(player, loyaltyProgram, choice -> {
                    if (FirstSecondChoice.FIRST.equals(choice)) {
                        pay(loyaltyProgram.spendSales(propertyManager.getOwner(), rent));
                    } else {
                        loyaltyProgram.gatherSales(propertyManager.getOwner(), rent);
                        pay(rent);
                    }
                });
            }
        } else {
            pay(rent);
        }

    }

    private void pay(int price) {
        PropertyManager propertyManager = propertyController.getManager(property);
        payCommandBuilder
                .addDebtor(player)
                .addCreditor(propertyManager.getOwner())
                .setMoney(price)
                .build()
                .execute();
    }
}
