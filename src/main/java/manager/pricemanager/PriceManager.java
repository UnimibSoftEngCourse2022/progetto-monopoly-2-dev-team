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
    public abstract int getRent(PropertyModel property);
}
