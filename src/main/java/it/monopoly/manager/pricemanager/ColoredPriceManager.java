package it.monopoly.manager.pricemanager;

import it.monopoly.manager.randomizer.PropertyRandomizerManager;
import it.monopoly.model.PropertyCategoryMapper;
import it.monopoly.model.PropertyOwnerMapper;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyModel;

import java.util.List;

public class ColoredPriceManager extends PriceManager {

    public ColoredPriceManager(PropertyModel property,
                               PropertyRandomizerManager propertyRandomizerManager,
                               PropertyOwnerMapper propertyOwnerMapper,
                               PropertyCategoryMapper propertyCategoryMapper) {
        super(property, propertyRandomizerManager, propertyOwnerMapper, propertyCategoryMapper);
    }

    @Override
    protected int getCleanRent() {
        int rent;
        if (property.getHotelNumber() != 0) {
            rent = property.getHotelRent();
        } else if (property.getHouseNumber() != 0) {
            rent = property.getHouseRent();
        } else {
            rent = property.getBaseRent();
            PlayerModel player = propertyOwnerMapper.getOwner(property);
            if (player != null) {
                List<PropertyModel> categoryProperties = propertyCategoryMapper
                        .getCategoryProperties(property.getCategory());
                boolean sameOwner = true;
                for (int i = 0; i < categoryProperties.size() && sameOwner; i++) {
                    sameOwner = player.equals(propertyOwnerMapper.getOwner(categoryProperties.get(i)));
                }
                if (sameOwner) {
                    rent *= 2;
                }
            }
        }
        return rent;
    }


    @Override
    public int getHousePrice() {
        return property.getHousePrice();
    }

    @Override
    public int getHotelPrice() {
        return property.getHotelPrice();
    }
}
