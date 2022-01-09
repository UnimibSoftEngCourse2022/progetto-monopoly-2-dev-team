package manager.pricemanager;

import model.PropertyCategoryMapper;
import model.PropertyOwnerMapper;
import model.player.PlayerModel;
import model.property.PropertyModel;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ColoredPriceManager extends PriceManager {

    //TODO Add randomization logic

    public ColoredPriceManager(PropertyOwnerMapper propertyOwnerMapper, PropertyCategoryMapper propertyCategoryMapper) {
        super(propertyOwnerMapper, propertyCategoryMapper);
    }

    @Override
    public int getPrice(PropertyModel property) {
        return property.getPrice();
    }

    @Override
    public int getRent(PropertyModel property) {
        return getCleanRent(property);
    }

    private int getCleanRent(PropertyModel property) {
        int rent;
        if (property.getHouseNumber() == 0) {
            if (property.getHotelNumber() == 0) {
                rent = property.getBaseRent();
                PlayerModel player = propertyOwnerMapper.getOwner(property);
                if (player != null) {
                    List<PropertyModel> categoryProperties = propertyCategoryMapper
                            .getCategoryProperties(property.getCategory());
                    boolean sameOwner = true;
                    for (int i = 0; i < categoryProperties.size() && sameOwner; i++) {
                        sameOwner = player.equals(propertyOwnerMapper.getOwner(categoryProperties.get(i)));
                    }
                }
            } else {
                rent = property.getHotelRent();
            }
        } else {
            rent = property.getHouseRent();
        }
        return rent;
    }
}
