package it.monopoly.manager.pricemanager;

import it.monopoly.manager.randomizer.PropertyRandomizerManager;
import it.monopoly.manager.randomizer.RandomizationManager;
import it.monopoly.manager.randomizer.TaxRandomizerManager;
import it.monopoly.model.PropertyCategoryMapper;
import it.monopoly.model.PropertyOwnerMapper;
import it.monopoly.model.PropertyRandomizeModel;
import it.monopoly.model.property.PropertyModel;

public abstract class PriceManager {
    protected final PropertyModel property;
    protected final PropertyRandomizerManager propertyRandomizerManager;
    protected final PropertyOwnerMapper propertyOwnerMapper;
    protected final PropertyCategoryMapper propertyCategoryMapper;
    private final TaxRandomizerManager taxRandomizerManager;

    protected PriceManager(PropertyModel property,
                           RandomizationManager randomizationManager,
                           PropertyOwnerMapper propertyOwnerMapper,
                           PropertyCategoryMapper propertyCategoryMapper) {
        this.property = property;
        if (randomizationManager != null) {
            this.propertyRandomizerManager = randomizationManager.getPropertyRandomizerManager();
            this.taxRandomizerManager = randomizationManager.getTaxRandomizerManager();
        } else {
            this.propertyRandomizerManager = null;
            this.taxRandomizerManager = null;
        }
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

    public int getLiftMortgageValue() {
        int mortgageValue = property.getMortgageValue();
        float randomValue = 0;
        if (propertyRandomizerManager != null) {
            randomValue = mortgageValue * getRandomizeModel().getMortgagePercentage();
        }

        float value = mortgageValue + randomValue;
        float randomTax = 0;
        if (taxRandomizerManager != null) {
            randomTax = taxRandomizerManager.getTaxesPercentage();
        }
        value += value * (.1f * randomTax);
        return (int) value;
    }

    public int getRent() {
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

    public String getRentString() {
        return String.valueOf(getRent());
    }

    protected abstract int getCleanRent();
}
