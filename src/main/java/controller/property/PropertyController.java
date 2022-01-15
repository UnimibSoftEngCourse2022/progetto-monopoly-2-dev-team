package controller.property;

import controller.command.ModelCommand;
import controller.ManagerController;
import controller.property.command.BuildHotelCommand;
import controller.property.command.BuildHouseCommand;
import controller.property.command.MortgageCommand;
import manager.property.PropertyManager;
import model.PropertyCategoryMapper;
import model.PropertyOwnerMapper;
import model.property.PropertyModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PropertyController extends ManagerController<PropertyModel, PropertyManager> {
    private final List<PropertyModel> properties;
    private final Map<PropertyModel, PropertyManager> propertyManagerMap = new ConcurrentHashMap<>();

    public PropertyController(List<PropertyModel> properties,
                              PropertyOwnerMapper ownerMapper,
                              PropertyCategoryMapper categoryMapper) {
        this.properties = properties;
        for (PropertyModel property : properties) {
            propertyManagerMap.put(
                    property,
                    new PropertyManager(property, ownerMapper, categoryMapper)
            );
        }
    }

    public PropertyManager getManager(PropertyModel property) {
        return propertyManagerMap.getOrDefault(property, null);
    }

    public List<PropertyModel> getModels() {
        return properties;
    }

    @Override
    public List<ModelCommand<PropertyModel>> getCommands(PropertyModel model) {
        List<ModelCommand<PropertyModel>> modelCommands = new ArrayList<>();
        if (model.isImprovable()) {
            modelCommands.add(new BuildHouseCommand(this));
            modelCommands.add(new BuildHotelCommand(this));
        }
        modelCommands.add(new MortgageCommand(this));
        return modelCommands;
    }
}
