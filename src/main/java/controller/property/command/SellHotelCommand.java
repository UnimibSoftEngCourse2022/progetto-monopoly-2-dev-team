package controller.property.command;

import controller.ManagerController;
import controller.TradeController;
import controller.command.ExecutableModelCommand;
import manager.property.PropertyManager;
import model.player.PlayerModel;
import model.property.PropertyModel;

public class SellHotelCommand extends ExecutableModelCommand<PropertyModel> {
    private final ManagerController<PropertyModel, PropertyManager> controller;

    public SellHotelCommand(ManagerController<PropertyModel, PropertyManager> controller) {
        this(controller, null);
    }

    public SellHotelCommand(ManagerController<PropertyModel, PropertyManager> controller, PropertyModel property) {
        super(property);
        this.controller = controller;
    }

    @Override
    public String getCommandName() {
        return "Sell Hotel";
    }

    @Override
    public boolean isEnabled(PropertyModel property) {
        PropertyManager manager = controller.getManager(property);
        return manager.canRemoveHotel();
    }

    @Override
    public void executableMethod(PropertyModel property) {
        PropertyManager manager = controller.getManager(property);
        manager.removeHotel();
    }
}
