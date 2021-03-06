package it.monopoly.controller.property.command;

import it.monopoly.controller.ManagerController;
import it.monopoly.controller.command.Command;
import it.monopoly.manager.pricemanager.PriceManager;
import it.monopoly.manager.property.PropertyManager;
import it.monopoly.model.property.PropertyModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MortgageCommand implements Command {
    private final Logger logger = LogManager.getLogger(getClass());

    private final ManagerController<PropertyModel, PropertyManager> controller;
    private final PropertyModel property;

    public MortgageCommand(ManagerController<PropertyModel, PropertyManager> controller, PropertyModel property) {
        this.controller = controller;
        this.property = property;
    }


    @Override
    public String getCommandName() {
        PriceManager priceManager = controller.getManager(property).getPriceManager();
        return property.isMortgaged() ?
                "Lift Mortgage: " + priceManager.getLiftMortgageValue()
                :
                "Mortgage: " + priceManager.getMortgageValue();
    }

    @Override
    public boolean isEnabled() {
        PropertyManager manager = controller.getManager(property);
        return property.isMortgaged() ? manager.canLiftMortgage() : manager.canMortgage();
    }

    @Override
    public void execute() {
        PropertyManager manager = controller.getManager(property);
        if (property.isMortgaged()) {
            logger.info("Lifting mortgage for property {}", property.getName());
            manager.liftMortgage();
        } else {
            logger.info("Mortgaging property {}", property.getName());
            manager.mortgage();
        }
    }
}
