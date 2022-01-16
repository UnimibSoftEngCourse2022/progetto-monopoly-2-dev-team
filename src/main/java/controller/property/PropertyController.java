package controller.property;

import controller.ManagerController;
import controller.command.Command;
import controller.command.CommandBuilderDispatcher;
import controller.property.command.PropertyCommandBuilder;
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
    private final CommandBuilderDispatcher commandBuilderDispatcher;
    private final Map<PropertyModel, PropertyManager> propertyManagerMap = new ConcurrentHashMap<>();

    public PropertyController(List<PropertyModel> properties,
                              PropertyOwnerMapper ownerMapper,
                              PropertyCategoryMapper categoryMapper,
                              CommandBuilderDispatcher commandBuilderDispatcher) {
        this.properties = properties;
        this.commandBuilderDispatcher = commandBuilderDispatcher;
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
    public List<Command> getCommands(PropertyModel model) {
        List<Command> modelCommands = new ArrayList<>();
        PropertyCommandBuilder builder = commandBuilderDispatcher.createCommandBuilder(PropertyCommandBuilder.class).setProperty(model);
        modelCommands.add(builder.setType(PropertyCommandBuilder.Type.MORTGAGE).build());
        if (model.isImprovable()) {
            modelCommands.add(builder.setType(PropertyCommandBuilder.Type.BUILD_HOTEL).build());
            modelCommands.add(builder.setType(PropertyCommandBuilder.Type.BUILD_HOUSE).build());
            modelCommands.add(builder.setType(PropertyCommandBuilder.Type.SELL_HOUSE).build());
            modelCommands.add(builder.setType(PropertyCommandBuilder.Type.SELL_HOTEL).build());
        }
        return modelCommands;
    }
}
