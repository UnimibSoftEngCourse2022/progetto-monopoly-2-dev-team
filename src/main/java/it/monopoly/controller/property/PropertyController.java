package it.monopoly.controller.property;

import it.monopoly.controller.ManagerController;
import it.monopoly.manager.pricemanager.PriceManager;
import it.monopoly.manager.pricemanager.PriceManagerDispatcher;
import it.monopoly.manager.property.PropertyManager;
import it.monopoly.model.PropertyCategoryMapper;
import it.monopoly.model.PropertyOwnerMapper;
import it.monopoly.model.property.PropertyModel;

import java.util.List;

public class PropertyController extends ManagerController<PropertyModel, PropertyManager> {

    public PropertyController(List<PropertyModel> properties,
                              PriceManagerDispatcher priceManagerDispatcher,
                              PropertyOwnerMapper ownerMapper,
                              PropertyCategoryMapper categoryMapper) {
        this.models = properties;
        for (PropertyModel property : properties) {
            PriceManager priceManager = priceManagerDispatcher.getPriceManager(property);
            modelToManagerMap.put(
                    property,
                    new PropertyManager(property, priceManager, ownerMapper, categoryMapper)
            );
        }
    }

}
