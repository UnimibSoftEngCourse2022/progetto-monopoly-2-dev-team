package it.monopoly.manager.pricemanager;

import it.monopoly.controller.event.DiceRoller;
import it.monopoly.manager.randomizer.PropertyRandomizerManager;
import it.monopoly.model.PropertyCategoryMapper;
import it.monopoly.model.PropertyOwnerMapper;
import it.monopoly.model.property.PropertyCategory;
import it.monopoly.model.property.PropertyModel;

public class PriceManagerDispatcher {

    private final PropertyRandomizerManager propertyRandomizerManager;
    private final PropertyOwnerMapper propertyOwnerMapper;
    private final PropertyCategoryMapper propertyCategoryMapper;
    private final DiceRoller diceRoller;

    public PriceManagerDispatcher(
            PropertyRandomizerManager propertyRandomizerManager,
            PropertyOwnerMapper propertyOwnerMapper,
            PropertyCategoryMapper propertyCategoryMapper,
            DiceRoller diceRoller
    ) {
        this.propertyRandomizerManager = propertyRandomizerManager;
        this.propertyOwnerMapper = propertyOwnerMapper;
        this.propertyCategoryMapper = propertyCategoryMapper;
        this.diceRoller = diceRoller;
    }

    public PriceManager getPriceManager(PropertyModel property) {
        PropertyCategory category = property.getCategory();
        if (PropertyCategory.RAILROAD.equals(category)) {
            return new RailroadPriceManager(property,
                    propertyRandomizerManager,
                    propertyOwnerMapper,
                    propertyCategoryMapper);
        } else if (PropertyCategory.UTILITY.equals(category)) {
            return new UtilityPriceManager(property,
                    propertyRandomizerManager,
                    propertyOwnerMapper,
                    propertyCategoryMapper,
                    diceRoller);
        }
        return new ColoredPriceManager(property,
                propertyRandomizerManager,
                propertyOwnerMapper,
                propertyCategoryMapper);
    }
}
