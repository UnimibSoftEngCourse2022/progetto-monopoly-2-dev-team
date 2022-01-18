package it.monopoly.controller.property.command;

import it.monopoly.controller.ManagerController;
import it.monopoly.controller.command.Command;
import it.monopoly.manager.property.PropertyManager;
import it.monopoly.model.property.PropertyModel;

public class BuildHouseCommand implements Command {
    private final ManagerController<PropertyModel, PropertyManager> controller;
    private final PropertyModel property;

    public BuildHouseCommand(ManagerController<PropertyModel, PropertyManager> controller, PropertyModel property) {
        this.controller = controller;
        this.property = property;
    }

    @Override
    public String getCommandName() {
        return "Build House";
    }

    @Override
    public boolean isEnabled() {
        PropertyManager manager = controller.getManager(property);
        return manager.canImproveHouse();
    }

    @Override
    public void execute() {
        PropertyManager manager = controller.getManager(property);
        manager.buildHouse();
    }
}
