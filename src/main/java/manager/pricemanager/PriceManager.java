package manager.pricemanager;

import model.PropertyCategoryMapper;
import model.PropertyOwnerMapper;
import model.property.PropertyModel;

public abstract class PriceManager {
    protected final PropertyOwnerMapper propertyOwnerMapper;
    protected final PropertyCategoryMapper propertyCategoryMapper;

    public PriceManager(PropertyOwnerMapper propertyOwnerMapper, PropertyCategoryMapper propertyCategoryMapper) {
        this.propertyOwnerMapper = propertyOwnerMapper;
        this.propertyCategoryMapper = propertyCategoryMapper;
    }

    public abstract int getPrice(PropertyModel property);
    public abstract int getRent(PropertyModel property);
}
