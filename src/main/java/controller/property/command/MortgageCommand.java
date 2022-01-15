package controller.property.command;

import controller.command.ExecutableModelCommand;
import controller.ManagerController;
import manager.property.PropertyManager;
import model.property.PropertyModel;

public class MortgageCommand extends ExecutableModelCommand<PropertyModel> {
    private final ManagerController<PropertyModel, PropertyManager> controller;

    public MortgageCommand(ManagerController<PropertyModel, PropertyManager> controller) {
        this(controller, null);
    }

    public MortgageCommand(ManagerController<PropertyModel, PropertyManager> controller, PropertyModel property) {
        super(property);
        this.controller = controller;
    }

    @Override
    public String getCommandName() {
        return "Mortgage";
    }

    @Override
    public boolean isEnabled(PropertyModel property) {
        PropertyManager manager = controller.getManager(property);
        return manager.canMortgage();
    }

    @Override
    public void executableMethod(PropertyModel property) {
        PropertyManager manager = controller.getManager(property);
        manager.mortgage();
    }
}
