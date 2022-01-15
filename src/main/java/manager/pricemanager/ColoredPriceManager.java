package manager.pricemanager;

import model.PropertyCategoryMapper;
import model.PropertyOwnerMapper;
import model.player.PlayerModel;
import model.property.PropertyModel;

import java.util.List;

public class ColoredPriceManager extends PriceManager {

    //TODO Add randomization logic

    public ColoredPriceManager(PropertyOwnerMapper propertyOwnerMapper, PropertyCategoryMapper propertyCategoryMapper) {
        super(propertyOwnerMapper, propertyCategoryMapper);
    }

    @Override
    public int getRent(PropertyModel property) {
        return getCleanRent(property);
    }

    private int getCleanRent(PropertyModel property) {
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
                if(sameOwner) {
                    rent *= 2;
                }
            }
        }
        return rent;
    }

    public int getHousePrice(PropertyModel property) {
        return property.getHousePrice();
    }

    public int getHotelPrice(PropertyModel property) {
        return property.getHotelPrice();
    }
}
