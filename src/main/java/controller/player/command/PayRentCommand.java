package controller.player.command;

import controller.command.Command;
import controller.player.PlayerController;
import controller.property.PropertyController;
import manager.loyaltyprogram.LoyaltyProgram;
import manager.property.PropertyManager;
import model.player.PlayerModel;
import model.property.PropertyModel;

public class PayRentCommand implements Command {

    private final PropertyController propertyController;
    private final PlayerController playerController;
    private final PlayerModel player;
    private final PropertyModel property;
    private final PayCommandBuilder payCommandBuilder;

    public PayRentCommand(
            PropertyController propertyController,
            PlayerController playerController,
            PlayerModel player,
            PropertyModel property,
            PayCommandBuilder payCommandBuilder) {
        this.propertyController = propertyController;
        this.playerController = playerController;
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
        int rent = propertyManager.getPriceManager().getRent(property);
        int price = 0;

        if (player == null || propertyManager.getOwner() == null) {
            return;
        }

        if (player.equals(propertyManager.getOwner())) {
            return;
        }

        LoyaltyProgram loyaltyProgram = playerController.getManager(player).getLoyaltyProgram();

        if (loyaltyProgram != null) {
            if (loyaltyProgram.getType().equals(LoyaltyProgram.Type.PERCENTAGE)) {
                price = loyaltyProgram.spendSales(propertyManager.getOwner(), rent);
            } else {
                // TODO: implement player choice from frontend
                // for test
                price = rent;
            }
            loyaltyProgram.gatherSales(propertyManager.getOwner(), rent);
        } else {
            price = rent;
        }

        payCommandBuilder.addDebtor(player)
                .addCreditor(propertyManager.getOwner())
                .setMoney(price)
                .build()
                .execute();
    }
}
