package controller.property;

import manager.property.PropertyManager;
import model.PropertyCategoryMapper;
import model.PropertyOwnerMapper;
import model.property.PropertyModel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PropertyController {
    private Map<PropertyModel, PropertyManager> propertyManagerMap = new ConcurrentHashMap<>();

    public PropertyController(List<PropertyModel> properties,
                              PropertyOwnerMapper ownerMapper,
                              PropertyCategoryMapper categoryMapper) {
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
}
