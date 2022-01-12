package manager.pricemanager;

import model.PropertyCategoryMapper;
import model.PropertyOwnerMapper;
import model.property.PropertyModel;

public abstract class PriceManager {
    protected final PropertyOwnerMapper propertyOwnerMapper;
    protected final PropertyCategoryMapper propertyCategoryMapper;

    protected PriceManager(PropertyOwnerMapper propertyOwnerMapper, PropertyCategoryMapper propertyCategoryMapper) {
        this.propertyOwnerMapper = propertyOwnerMapper;
        this.propertyCategoryMapper = propertyCategoryMapper;
    }

    public int getPrice(PropertyModel property) {
        return property.getPrice();
    }

    public int getHousePrice(PropertyModel property) {
        return property.getHousePrice();
    }

    public int getHotelPrice(PropertyModel property) {
        return property.getHotelPrice();
    }

    public int getMortgageValue(PropertyModel property) {
        return property.getMortgageValue();
    }

    public abstract int getRent(PropertyModel property);
}
