package it.monopoly.controller.property.command;

import it.monopoly.controller.ManagerController;
import it.monopoly.controller.command.Command;
import it.monopoly.manager.property.PropertyManager;
import it.monopoly.model.property.PropertyModel;

public class SellHouseCommand implements Command {
    private final ManagerController<PropertyModel, PropertyManager> controller;
    private final PropertyModel property;

    public SellHouseCommand(ManagerController<PropertyModel, PropertyManager> controller, PropertyModel property) {
        this.controller = controller;
        this.property = property;
    }

    @Override
    public String getCommandName() {
        return "Sell House";
    }

    @Override
    public boolean isEnabled() {
        PropertyManager manager = controller.getManager(property);
        return manager.canRemoveHouse();
    }

    @Override
    public void execute() {
        PropertyManager manager = controller.getManager(property);
        manager.removeHouse();
    }
}
