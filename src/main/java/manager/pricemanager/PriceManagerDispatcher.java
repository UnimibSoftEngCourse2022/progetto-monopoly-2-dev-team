package manager.pricemanager;

import controller.DiceRoller;
import model.PropertyCategoryMapper;
import model.PropertyOwnerMapper;
import model.property.PropertyCategory;
import model.property.PropertyModel;

public class PriceManagerDispatcher extends PriceManager {
    private final RailroadPriceManager railroadPriceManager;
    private final UtilityPriceManager utilityPriceManager;
    private final ColoredPriceManager coloredPriceManager;

    protected PriceManagerDispatcher(
            PropertyOwnerMapper propertyOwnerMapper,
            PropertyCategoryMapper propertyCategoryMapper,
            DiceRoller diceRoller
    ) {
        super(propertyOwnerMapper, propertyCategoryMapper);
        this.railroadPriceManager = new RailroadPriceManager(propertyOwnerMapper, propertyCategoryMapper);
        this.utilityPriceManager = new UtilityPriceManager(propertyOwnerMapper, propertyCategoryMapper, diceRoller);
        this.coloredPriceManager = new ColoredPriceManager(propertyOwnerMapper, propertyCategoryMapper);
    }

    @Override
    public int getHousePrice(PropertyModel property) {
        return super.getHousePrice(property);
    }

    @Override
    public int getHotelPrice(PropertyModel property) {
        return super.getHotelPrice(property);
    }

    @Override
    public int getRent(PropertyModel property) {
        return getInstance(property).getRent(property);
    }

    private PriceManager getInstance(PropertyModel property) {
        PropertyCategory category = property.getCategory();
        if (PropertyCategory.RAILROAD.equals(category)) {
            return railroadPriceManager;
        } else if (PropertyCategory.UTILITY.equals(category)) {
            return utilityPriceManager;
        }
        return coloredPriceManager;
    }
}
