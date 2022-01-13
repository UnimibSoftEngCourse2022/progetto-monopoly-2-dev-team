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
    private static final int MAX_NUMBER_OF_HOUSES_IN_GAME = 32; //TODO Implement max number of houses/hotel in game
    private static final int MAX_NUMBER_OF_HOTEL_IN_GAME = 12;
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

    public boolean removeHouse(PropertyModel property) {
        if (property != null && canRemoveHouse(property)) {
            property.setHouseNumber(
                    property.getHouseNumber() - 1
            );
            return true;
        }
        return false;
    }

    public boolean removeHotel(PropertyModel property) {
        if (property != null && canRemoveHotel(property)) {
            property.setHotelNumber(0);
            property.setHouseNumber(4);
            return true;
        }
        return false;
    }

    public boolean mortgage(PropertyModel property) {
        if (!canMortgage(property)) {
            return false;
        }
        property.setMortgaged(true);
        return true;
    }

    public boolean liftMortgage(PropertyModel property) {
        if (!canLiftMortgage(property)) {
            return false;
        }
        property.setMortgaged(false);
        return true;
    }

    public boolean canImproveHouse(PropertyModel property) {
        return property != null &&
                hasRightCategory(property) &&
                hasValidBuildingsForHouse(property) &&
                ownerHasEveryPropertyInCategoryAndUnmortgaged(property) &&
                hasLessOrEqualBuildingsInCategoryForBuildingHouse(property);
    }

    public boolean canImproveHotel(PropertyModel property) {
        return property != null &&
                hasRightCategory(property) &&
                hasValidBuildingsForHotel(property) &&
                ownerHasEveryPropertyInCategoryAndUnmortgaged(property) &&
                hasLessOrEqualBuildingsInCategoryForBuildingHotel(property);
    }

    public boolean canRemoveHouse(PropertyModel property) {
        return property != null &&
                hasRightCategory(property) &&
                hasValidBuildingsForRemovingHouse(property) &&
                hasLessOrEqualBuildingsInCategoryForRemovingHouse(property);
    }

    public boolean canRemoveHotel(PropertyModel property) {
        return property != null &&
                hasRightCategory(property) &&
                hasValidBuildingsForRemovingHotel(property) &&
                hasLessOrEqualBuildingsInCategoryForRemovingHotel(property);
    }

    public boolean canMortgage(PropertyModel property) {
        return !property.isMortgaged() && canSell(property);
    }

    public boolean canSell(PropertyModel property) {
        return property != null && zeroBuildingsInCategory(property);
    }

    public boolean canCollectRent(PropertyModel property) {
        return !property.isMortgaged() && ownerMapper.getOwner(property) != null;
    }

    public boolean canLiftMortgage(PropertyModel property) {
        return property.isMortgaged();
    }

    public void setOwner(PropertyModel property, PlayerModel player) {
        ownerMapper.setOwner(property, player);
    }

//    public PlayerModel removeOwner(PropertyModel property) {
//        return ownerMapper.removeOwner(property);
//    }

    public PlayerModel getOwner(PropertyModel property) {
        return ownerMapper.getOwner(property);
    }

    public PriceManager getPriceManager() {
        return priceManager;
    }

    //PRIVATE METHODS

    private boolean hasRightCategory(PropertyModel property) {
        return property.isImprovable() && !property.isMortgaged();
    }

    private boolean ownerHasEveryPropertyInCategoryAndUnmortgaged(PropertyModel property) {
        List<PropertyModel> categoryProperties = categoryMapper
                .getCategoryProperties(property.getCategory());
        Stream<PropertyModel> ownerProperties = categoryProperties
                .stream()
                .filter(
                        propertyModel -> {
                            PlayerModel owner = ownerMapper.getOwner(property);
                            return owner != null && !property.isMortgaged() && owner.equals(ownerMapper.getOwner(propertyModel));
                        }
                );
        return categoryProperties.size() == ownerProperties.count();
    }

    private boolean zeroBuildingsInCategory(PropertyModel property) {
        boolean valid = true;
        List<PropertyModel> categoryProperties = categoryMapper
                .getCategoryProperties(property.getCategory());
        for (PropertyModel categoryProperty : categoryProperties) {
            valid &= categoryProperty.getHouseNumber() == 0 && categoryProperty.getHotelNumber() == 0;
        }
        return valid;
    }

    private boolean hasLessOrEqualBuildingsInCategoryForBuildingHouse(PropertyModel property) {
        return hasLessOrEqualBuildingsInCategoryForHouse(property, false);
    }

    private boolean hasLessOrEqualBuildingsInCategoryForRemovingHouse(PropertyModel property) {
        return hasLessOrEqualBuildingsInCategoryForHouse(property, true);
    }

    private boolean hasLessOrEqualBuildingsInCategoryForHouse(PropertyModel property, boolean removing) {
        boolean valid = true;
        List<PropertyModel> categoryProperties = categoryMapper
                .getCategoryProperties(property.getCategory());
        for (PropertyModel categoryProperty : categoryProperties) {
            if (removing) {
                valid &= property.getHouseNumber() >= categoryProperty.getHouseNumber();
            } else {
                valid &= property.getHouseNumber() <= categoryProperty.getHouseNumber();
            }
        }
        return valid;
    }

    private boolean hasValidBuildingsForHouse(PropertyModel property) {
        return property.getHotelNumber() == 0 && property.getHouseNumber() < MAX_NUMBER_OF_HOUSES;
    }

    private boolean hasValidBuildingsForRemovingHouse(PropertyModel property) {
        return property.getHotelNumber() == 0 && property.getHouseNumber() > 0;
    }

    private boolean hasLessOrEqualBuildingsInCategoryForBuildingHotel(PropertyModel property) {
        return hasLessOrEqualBuildingsInCategoryForHotel(property, false);
    }

    private boolean hasLessOrEqualBuildingsInCategoryForRemovingHotel(PropertyModel property) {
        return hasLessOrEqualBuildingsInCategoryForHotel(property, true);
    }

    private boolean hasLessOrEqualBuildingsInCategoryForHotel(PropertyModel property, boolean removing) {
        boolean valid = true;
        List<PropertyModel> categoryProperties = categoryMapper
                .getCategoryProperties(property.getCategory());
        for (PropertyModel categoryProperty : categoryProperties) {
            if (removing) {
                valid &= categoryProperty.getHouseNumber() == MAX_NUMBER_OF_HOUSES ||
                        property.getHotelNumber() == categoryProperty.getHotelNumber();
            } else {
                valid &= categoryProperty.getHouseNumber() == MAX_NUMBER_OF_HOUSES ||
                        property.getHotelNumber() <= categoryProperty.getHotelNumber();
            }
        }
        return valid;
    }

    private boolean hasValidBuildingsForHotel(PropertyModel property) {
        return property.getHouseNumber() == MAX_NUMBER_OF_HOUSES && property.getHotelNumber() < MAX_NUMBER_OF_HOTEL;
    }

    private boolean hasValidBuildingsForRemovingHotel(PropertyModel property) {
        return property.getHouseNumber() == 0 && property.getHotelNumber() > 0;
    }
}
