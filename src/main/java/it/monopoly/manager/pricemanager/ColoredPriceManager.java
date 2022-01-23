package it.monopoly.manager.pricemanager;

import it.monopoly.model.PropertyCategoryMapper;
import it.monopoly.model.PropertyOwnerMapper;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyModel;

import java.util.List;

public class ColoredPriceManager extends PriceManager {

    //TODO Add randomization logic

    public ColoredPriceManager(PropertyModel property, PropertyOwnerMapper propertyOwnerMapper, PropertyCategoryMapper propertyCategoryMapper) {
        super(property, propertyOwnerMapper, propertyCategoryMapper);
    }

    @Override
    public int getRent() {
        return getCleanRent();
    }

    private int getCleanRent() {
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
