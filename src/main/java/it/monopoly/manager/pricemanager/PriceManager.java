package it.monopoly.manager.pricemanager;

import it.monopoly.manager.randomizer.PropertyRandomizerManager;
import it.monopoly.model.PropertyCategoryMapper;
import it.monopoly.model.PropertyOwnerMapper;
import it.monopoly.model.PropertyRandomizeModel;
import it.monopoly.model.property.PropertyModel;

public abstract class PriceManager {
    protected final PropertyModel property;
    protected final PropertyRandomizerManager propertyRandomizerManager;
    protected final PropertyOwnerMapper propertyOwnerMapper;
    protected final PropertyCategoryMapper propertyCategoryMapper;

    protected PriceManager(PropertyModel property,
                           PropertyRandomizerManager propertyRandomizerManager,
                           PropertyOwnerMapper propertyOwnerMapper,
                           PropertyCategoryMapper propertyCategoryMapper) {
        this.property = property;
        this.propertyRandomizerManager = propertyRandomizerManager;
        this.propertyOwnerMapper = propertyOwnerMapper;
        this.propertyCategoryMapper = propertyCategoryMapper;
    }

    public int getPrice() {
        int price = property.getPrice();
        float randomValue = 0;
        if (propertyRandomizerManager != null) {
            randomValue = price * getRandomizeModel().getPricePercentage();
        }
        return (int) (price + randomValue);
    }

    public int getHousePrice() {
        int housePrice = property.getHousePrice();
        float randomValue = 0;
        if (propertyRandomizerManager != null) {
            randomValue = housePrice * getRandomizeModel().getHousePricePercentage();
        }
        return (int) (housePrice + randomValue);
    }

    public int getHotelPrice() {

        int hotelPrice = property.getHotelPrice();
        float randomValue = 0;
        if (propertyRandomizerManager != null) {
            randomValue = hotelPrice * getRandomizeModel().getHotelPricePercentage();
        }
        return (int) (hotelPrice + randomValue);
    }

    public int getMortgageValue() {
        int mortgageValue = property.getMortgageValue();
        float randomValue = 0;
        if (propertyRandomizerManager != null) {
            randomValue = mortgageValue * getRandomizeModel().getMortgagePercentage();
        }
        return (int) (mortgageValue + randomValue);
    }

    public final int getRent() {
        int rent = getCleanRent();
        float randomValue = 0;
        if (propertyRandomizerManager != null) {
            randomValue = rent * getRandomizeModel().getRentPercentage();
        }
        return (int) (rent + randomValue);
    }

    private PropertyRandomizeModel getRandomizeModel() {
        return propertyRandomizerManager
                .getPropertyRandomize(property);
    }

    protected abstract int getCleanRent();
}
