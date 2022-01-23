package it.monopoly.manager.pricemanager;

import it.monopoly.model.PropertyCategoryMapper;
import it.monopoly.model.PropertyOwnerMapper;
import it.monopoly.model.property.PropertyModel;

public abstract class PriceManager {
    protected final PropertyModel property;
    protected final PropertyOwnerMapper propertyOwnerMapper;
    protected final PropertyCategoryMapper propertyCategoryMapper;

    protected PriceManager(PropertyModel property, PropertyOwnerMapper propertyOwnerMapper, PropertyCategoryMapper propertyCategoryMapper) {
        this.property = property;
        this.propertyOwnerMapper = propertyOwnerMapper;
        this.propertyCategoryMapper = propertyCategoryMapper;
    }

    public int getPrice() {
        return property.getPrice();
    }

    public int getHousePrice() {
        return property.getHousePrice();
    }

    public int getHotelPrice() {
        return property.getHotelPrice();
    }

    public int getMortgageValue() {
        return property.getMortgageValue();
    }

    public abstract int getRent();
}
