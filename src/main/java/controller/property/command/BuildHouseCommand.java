package controller.property.command;

import controller.command.ExecutableModelCommand;
import controller.ManagerController;
import manager.property.PropertyManager;
import model.property.PropertyModel;

public class BuildHouseCommand extends ExecutableModelCommand<PropertyModel> {
    private final ManagerController<PropertyModel, PropertyManager> controller;

    public BuildHouseCommand(ManagerController<PropertyModel, PropertyManager> controller) {
        this(controller, null);
    }

    public BuildHouseCommand(ManagerController<PropertyModel, PropertyManager> controller, PropertyModel property) {
        super(property);
        this.controller = controller;
    }

    @Override
    public String getCommandName() {
        return "Build House";
    }

    @Override
    public boolean isEnabled(PropertyModel property) {
        PropertyManager manager = controller.getManager(property);
        return manager.canImproveHouse();
    }

    @Override
    public void executableMethod(PropertyModel property) {
        PropertyManager manager = controller.getManager(property);
        manager.buildHouse();
    }
}
