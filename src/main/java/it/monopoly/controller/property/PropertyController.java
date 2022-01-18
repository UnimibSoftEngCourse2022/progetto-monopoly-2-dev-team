package it.monopoly.controller.property;

import it.monopoly.controller.ManagerController;
import it.monopoly.manager.property.PropertyManager;
import it.monopoly.model.PropertyCategoryMapper;
import it.monopoly.model.PropertyOwnerMapper;
import it.monopoly.model.property.PropertyModel;

import java.util.List;

public class PropertyController extends ManagerController<PropertyModel, PropertyManager> {

    public PropertyController(List<PropertyModel> properties,
                              PropertyOwnerMapper ownerMapper,
                              PropertyCategoryMapper categoryMapper) {
        this.models = properties;
        for (PropertyModel property : properties) {
            modelToManagerMap.put(
                    property,
                    new PropertyManager(property, ownerMapper, categoryMapper)
            );
        }
    }

}
