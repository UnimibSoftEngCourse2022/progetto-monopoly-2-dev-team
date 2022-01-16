package controller.property.command;

import controller.ManagerController;
import controller.TradeController;
import controller.command.ExecutableModelCommand;
import manager.property.PropertyManager;
import model.player.PlayerModel;
import model.property.PropertyModel;

public class SellHouseCommand extends ExecutableModelCommand<PropertyModel> {
    private final ManagerController<PropertyModel, PropertyManager> controller;

    public SellHouseCommand(ManagerController<PropertyModel, PropertyManager> controller) {
        this(controller, null);
    }

    public SellHouseCommand(ManagerController<PropertyModel, PropertyManager> controller, PropertyModel property) {
        super(property);
        this.controller = controller;
    }

    @Override
    public String getCommandName() {
        return "Sell House";
    }

    @Override
    public boolean isEnabled(PropertyModel property) {
        PropertyManager manager = controller.getManager(property);
        return manager.canRemoveHouse();
    }

    @Override
    public void executableMethod(PropertyModel property) {
        PropertyManager manager = controller.getManager(property);
        manager.removeHouse();
    }
}
