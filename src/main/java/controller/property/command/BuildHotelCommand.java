package controller.property.command;

import controller.ManagerController;
import controller.command.Command;
import manager.property.PropertyManager;
import model.property.PropertyModel;

public class BuildHotelCommand implements Command {
    private final ManagerController<PropertyModel, PropertyManager> controller;
    private final PropertyModel property;

    public BuildHotelCommand(ManagerController<PropertyModel, PropertyManager> controller, PropertyModel property) {
        this.controller = controller;
        this.property = property;
    }

    @Override
    public String getCommandName() {
        return "Build Hotel";
    }

    @Override
    public boolean isEnabled() {
        PropertyManager manager = controller.getManager(property);
        return manager.canImproveHotel();
    }

    @Override
    public void execute() {
        PropertyManager manager = controller.getManager(property);
        manager.buildHotel();
    }
}
