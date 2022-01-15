package controller.property.command;

import controller.command.ExecutableModelCommand;
import controller.ManagerController;
import manager.property.PropertyManager;
import model.property.PropertyModel;

public class BuildHotelCommand extends ExecutableModelCommand<PropertyModel> {
    private final ManagerController<PropertyModel, PropertyManager> controller;

    public BuildHotelCommand(ManagerController<PropertyModel, PropertyManager> controller) {
        this(controller, null);
    }

    public BuildHotelCommand(ManagerController<PropertyModel, PropertyManager> controller, PropertyModel property) {
        super(property);
        this.controller = controller;
    }

    @Override
    public String getCommandName() {
        return "Build Hotel";
    }

    @Override
    public boolean isEnabled(PropertyModel property) {
        PropertyManager manager = controller.getManager(property);
        return manager.canImproveHotel();
    }

    @Override
    public void executableMethod(PropertyModel property) {
        PropertyManager manager = controller.getManager(property);
        manager.buildHotel();
    }
}
