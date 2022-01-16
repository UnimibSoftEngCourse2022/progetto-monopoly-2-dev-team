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
    private final CommandBuilderDispatcher commandBuilderDispatcher;

    public PropertyController(List<PropertyModel> properties,
                              PropertyOwnerMapper ownerMapper,
                              PropertyCategoryMapper categoryMapper,
                              CommandBuilderDispatcher commandBuilderDispatcher) {
        this.models = properties;
        this.commandBuilderDispatcher = commandBuilderDispatcher;
        for (PropertyModel property : properties) {
            modelToManagerMap.put(
                    property,
                    new PropertyManager(property, ownerMapper, categoryMapper)
            );
        }
    }

    @Override
    public List<Command> getCommands(PropertyModel property) {
        List<Command> commands = new ArrayList<>();
        PropertyCommandBuilder builder = commandBuilderDispatcher.createCommandBuilder(PropertyCommandBuilder.class).setProperty(property);
        commands.add(builder.setType(PropertyCommandBuilder.Type.MORTGAGE).build());
        if (property.isImprovable()) {
            commands.add(builder.setType(PropertyCommandBuilder.Type.BUILD_HOTEL).build());
            commands.add(builder.setType(PropertyCommandBuilder.Type.BUILD_HOUSE).build());
            commands.add(builder.setType(PropertyCommandBuilder.Type.SELL_HOUSE).build());
            commands.add(builder.setType(PropertyCommandBuilder.Type.SELL_HOTEL).build());
        }
        return commands;
    }
}
