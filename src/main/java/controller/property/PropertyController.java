package controller.property;

import controller.ManagerController;
import manager.property.PropertyManager;
import model.PropertyCategoryMapper;
import model.PropertyOwnerMapper;
import model.property.PropertyModel;

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
