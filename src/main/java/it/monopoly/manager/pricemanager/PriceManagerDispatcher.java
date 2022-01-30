package it.monopoly.manager.pricemanager;

import it.monopoly.controller.event.DiceRoller;
import it.monopoly.manager.randomizer.RandomizationManager;
import it.monopoly.model.PropertyCategoryMapper;
import it.monopoly.model.PropertyOwnerMapper;
import it.monopoly.model.property.PropertyCategory;
import it.monopoly.model.property.PropertyModel;

public class PriceManagerDispatcher {

    private final PropertyOwnerMapper propertyOwnerMapper;
    private final RandomizationManager randomizationManager;
    private final PropertyCategoryMapper propertyCategoryMapper;
    private final DiceRoller diceRoller;

    public PriceManagerDispatcher(
            RandomizationManager randomizationManager,
            PropertyOwnerMapper propertyOwnerMapper,
            PropertyCategoryMapper propertyCategoryMapper,
            DiceRoller diceRoller
    ) {
        this.randomizationManager = randomizationManager;
        this.propertyOwnerMapper = propertyOwnerMapper;
        this.propertyCategoryMapper = propertyCategoryMapper;
        this.diceRoller = diceRoller;
    }

    public PriceManager getPriceManager(PropertyModel property) {
        PropertyCategory category = property.getCategory();
        if (PropertyCategory.RAILROAD.equals(category)) {
            return new RailroadPriceManager(property,
                    randomizationManager,
                    propertyOwnerMapper,
                    propertyCategoryMapper);
        } else if (PropertyCategory.UTILITY.equals(category)) {
            return new UtilityPriceManager(property,
                    randomizationManager,
                    propertyOwnerMapper,
                    propertyCategoryMapper,
                    diceRoller);
        }
        return new ColoredPriceManager(property,
                randomizationManager,
                propertyOwnerMapper,
                propertyCategoryMapper);
    }
}
