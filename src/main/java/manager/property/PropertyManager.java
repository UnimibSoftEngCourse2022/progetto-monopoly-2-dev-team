package manager.property;

import manager.pricemanager.PriceManager;
import model.PropertyCategoryMapper;
import model.PropertyOwnerMapper;
import model.player.PlayerModel;
import model.property.PropertyModel;

import java.util.List;
import java.util.stream.Stream;

public class PropertyManager {
    private PropertyOwnerMapper ownerMapper;
    private PropertyCategoryMapper categoryMapper;
    private PriceManager priceManager;
    private static final int MAX_NUMBER_OF_HOUSES = 4;
    private static final int MAX_NUMBER_OF_HOTEL = 1;

    public PropertyManager(PropertyOwnerMapper ownerMapper, PropertyCategoryMapper categoryMapper, PriceManager priceManager) {
        this.ownerMapper = ownerMapper;
        this.categoryMapper = categoryMapper;
        this.priceManager = priceManager;
    }

    public boolean buildHouse(PropertyModel property) {
        if (property != null && canImproveHouse(property)) {
            property.setHouseNumber(
                    property.getHouseNumber() + 1
            );
            return true;
        }
        return false;
    }

    public boolean buildHotel(PropertyModel property) {
        if (property != null && canImproveHotel(property)) {
            property.setHotelNumber(1);
            property.setHouseNumber(0);
            return true;
        }
        return false;
    }

    public boolean canImproveHouse(PropertyModel property) {
        return property != null &&
                hasRightCategory(property) &&
                propertyHasValidBuildingsForHouses(property) &&
                ownerHasEveryPropertyInCategory(property) &&
                hasLessOrEqualNumberOfBuildingsInCategoryForHouse(property);
    }

    public boolean canImproveHotel(PropertyModel property) {
        return property != null &&
                hasRightCategory(property) &&
                hasValidBuildingsForHotel(property) &&
                ownerHasEveryPropertyInCategory(property) &&
                hasLessOrEqualNumberOfBuildingsInCategoryForHotel(property);
    }

    private boolean hasRightCategory(PropertyModel property) {
        return property.isImprovable();
    }

    private boolean ownerHasEveryPropertyInCategory(PropertyModel property) {
        List<PropertyModel> categoryProperties = categoryMapper
                .getCategoryProperties(property.getCategory());
        Stream<PropertyModel> ownerProperties = categoryProperties
                .stream()
                .filter(
                        propertyModel -> {
                            PlayerModel owner = ownerMapper.getOwner(property);
                            return owner != null && owner.equals(ownerMapper.getOwner(propertyModel));
                        }
                );
        return categoryProperties.size() == ownerProperties.count();
    }

    private boolean hasLessOrEqualNumberOfBuildingsInCategoryForHouse(PropertyModel property) {
        boolean valid = true;
        List<PropertyModel> categoryProperties = categoryMapper
                .getCategoryProperties(property.getCategory());
        for (PropertyModel categoryProperty : categoryProperties) {
            valid &= property.getHouseNumber() <= categoryProperty.getHouseNumber();
        }
        return valid;
    }

    private boolean propertyHasValidBuildingsForHouses(PropertyModel property) {
        return property.getHotelNumber() == 0 && property.getHouseNumber() < MAX_NUMBER_OF_HOUSES;
    }

    private boolean hasLessOrEqualNumberOfBuildingsInCategoryForHotel(PropertyModel property) {
        boolean valid = true;
        List<PropertyModel> categoryProperties = categoryMapper
                .getCategoryProperties(property.getCategory());
        for (PropertyModel categoryProperty : categoryProperties) {
            valid &= categoryProperty.getHouseNumber() == MAX_NUMBER_OF_HOUSES ||
                    property.getHotelNumber() <= categoryProperty.getHotelNumber();
        }
        return valid;
    }

    private boolean hasValidBuildingsForHotel(PropertyModel property) {
        return property.getHouseNumber() == MAX_NUMBER_OF_HOUSES && property.getHotelNumber() < MAX_NUMBER_OF_HOTEL;
    }
}
