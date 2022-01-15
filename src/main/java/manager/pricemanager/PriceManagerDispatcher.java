package manager.pricemanager;

import controller.DiceRoller;
import model.PropertyCategoryMapper;
import model.PropertyOwnerMapper;
import model.property.PropertyCategory;
import model.property.PropertyModel;

public class PriceManagerDispatcher {

    private final PropertyOwnerMapper propertyOwnerMapper;
    private final PropertyCategoryMapper propertyCategoryMapper;
    private final DiceRoller diceRoller;

    public PriceManagerDispatcher(
            PropertyOwnerMapper propertyOwnerMapper,
            PropertyCategoryMapper propertyCategoryMapper,
            DiceRoller diceRoller
    ) {
        this.propertyOwnerMapper = propertyOwnerMapper;
        this.propertyCategoryMapper = propertyCategoryMapper;
        this.diceRoller= diceRoller;
    }

    public PriceManager getPriceManager(PropertyModel property) {
        PropertyCategory category = property.getCategory();
        if (PropertyCategory.RAILROAD.equals(category)) {
            return new RailroadPriceManager(propertyOwnerMapper, propertyCategoryMapper);
        } else if (PropertyCategory.UTILITY.equals(category)) {
            return new UtilityPriceManager(propertyOwnerMapper, propertyCategoryMapper, diceRoller);
        }
        return new ColoredPriceManager(propertyOwnerMapper, propertyCategoryMapper);
    }
}
