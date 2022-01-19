package it.monopoly.controller.property.command;

import it.monopoly.controller.ManagerController;
import it.monopoly.controller.command.Command;
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
        return "Mortgage";
    }

    @Override
    public boolean isEnabled() {
        PropertyManager manager = controller.getManager(property);
        return manager.canMortgage();
    }

    @Override
    public void execute() {
        logger.info("Mortgaging property {}", property.getName());
        PropertyManager manager = controller.getManager(property);
        manager.mortgage();
    }
}
