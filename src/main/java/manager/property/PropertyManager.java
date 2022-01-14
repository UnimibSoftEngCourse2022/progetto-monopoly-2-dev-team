package manager.property;

import manager.pricemanager.PriceManager;
import model.PropertyCategoryMapper;
import model.PropertyOwnerMapper;
import model.player.PlayerModel;
import model.property.PropertyModel;

import java.util.List;
import java.util.stream.Stream;

public class PropertyManager {
    private final PropertyModel property;
    private PropertyOwnerMapper ownerMapper;
    private PropertyCategoryMapper categoryMapper;
    private PriceManager priceManager;
    private static final int MAX_NUMBER_OF_HOUSES_IN_GAME = 32; //TODO Implement max number of houses/hotel in game
    private static final int MAX_NUMBER_OF_HOTEL_IN_GAME = 12;
    private static final int MAX_NUMBER_OF_HOUSES = 4;
    private static final int MAX_NUMBER_OF_HOTEL = 1;

    public PropertyManager(PropertyModel property, PropertyOwnerMapper ownerMapper, PropertyCategoryMapper categoryMapper) {
        this.property = property;
        this.ownerMapper = ownerMapper;
        this.categoryMapper = categoryMapper;
        //this.priceManager = ; //TODO create category based price manager
    }

    public boolean buildHouse() {
        if (property != null && canImproveHouse()) {
            property.setHouseNumber(
                    property.getHouseNumber() + 1
            );
            return true;
        }
        return false;
    }

    public boolean buildHotel() {
        if (property != null && canImproveHotel()) {
            property.setHotelNumber(1);
            property.setHouseNumber(0);
            return true;
        }
        return false;
    }

    public boolean removeHouse() {
        if (property != null && canRemoveHouse()) {
            property.setHouseNumber(
                    property.getHouseNumber() - 1
            );
            return true;
        }
        return false;
    }

    public boolean removeHotel() {
        if (property != null && canRemoveHotel()) {
            property.setHotelNumber(0);
            property.setHouseNumber(4);
            return true;
        }
        return false;
    }

    public boolean mortgage() {
        if (!canMortgage()) {
            return false;
        }
        property.setMortgaged(true);
        return true;
    }

    public boolean liftMortgage() {
        if (!canLiftMortgage()) {
            return false;
        }
        property.setMortgaged(false);
        return true;
    }

    public boolean canImproveHouse() {
        return property != null &&
                hasRightCategory() &&
                hasValidBuildingsForHouse() &&
                ownerHasEveryPropertyInCategoryAndUnmortgaged() &&
                hasLessOrEqualBuildingsInCategoryForBuildingHouse();
    }

    public boolean canImproveHotel() {
        return property != null &&
                hasRightCategory() &&
                hasValidBuildingsForHotel() &&
                ownerHasEveryPropertyInCategoryAndUnmortgaged() &&
                hasLessOrEqualBuildingsInCategoryForBuildingHotel();
    }

    public boolean canRemoveHouse() {
        return property != null &&
                hasRightCategory() &&
                hasValidBuildingsForRemovingHouse() &&
                hasLessOrEqualBuildingsInCategoryForRemovingHouse();
    }

    public boolean canRemoveHotel() {
        return hasRightCategory() &&
                hasValidBuildingsForRemovingHotel() &&
                hasLessOrEqualBuildingsInCategoryForRemovingHotel();
    }

    public boolean canMortgage() {
        return !property.isMortgaged() && canSell();
    }

    public boolean canSell() {
        return zeroBuildingsInCategory();
    }

    public boolean canCollectRent() {
        return !property.isMortgaged() && ownerMapper.getOwner(property) != null;
    }

    public boolean canLiftMortgage() {
        return property.isMortgaged();
    }

    public void setOwner(PlayerModel player) {
        ownerMapper.setOwner(property, player);
    }

    public PlayerModel removeOwner() {
        return ownerMapper.removeOwner(property);
    }

    public PlayerModel getOwner() {
        return ownerMapper.getOwner(property);
    }

    public PriceManager getPriceManager() {
        return priceManager;
    }

    //PRIVATE METHODS

    private boolean hasRightCategory() {
        return property.isImprovable() && !property.isMortgaged();
    }

    private boolean ownerHasEveryPropertyInCategoryAndUnmortgaged() {
        List<PropertyModel> categoryProperties = categoryMapper
                .getCategoryProperties(property.getCategory());
        PlayerModel owner = ownerMapper.getOwner(property);
        if(owner == null) {
            return false;
        }
        Stream<PropertyModel> ownerProperties = categoryProperties
                .stream()
                .filter(
                        propertyModel -> !property.isMortgaged() && owner.equals(ownerMapper.getOwner(propertyModel))
                );
        return categoryProperties.size() == ownerProperties.count();
    }

    private boolean zeroBuildingsInCategory() {
        boolean valid = true;
        List<PropertyModel> categoryProperties = categoryMapper
                .getCategoryProperties(property.getCategory());
        for (PropertyModel categoryProperty : categoryProperties) {
            valid &= categoryProperty.getHouseNumber() == 0 && categoryProperty.getHotelNumber() == 0;
        }
        return valid;
    }

    private boolean hasLessOrEqualBuildingsInCategoryForBuildingHouse() {
        return hasLessOrEqualBuildingsInCategoryForHouse(false);
    }

    private boolean hasLessOrEqualBuildingsInCategoryForRemovingHouse() {
        return hasLessOrEqualBuildingsInCategoryForHouse(true);
    }

    private boolean hasLessOrEqualBuildingsInCategoryForHouse(boolean removing) {
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

    private boolean hasValidBuildingsForHouse() {
        return property.getHotelNumber() == 0 && property.getHouseNumber() < MAX_NUMBER_OF_HOUSES;
    }

    private boolean hasValidBuildingsForRemovingHouse() {
        return property.getHotelNumber() == 0 && property.getHouseNumber() > 0;
    }

    private boolean hasLessOrEqualBuildingsInCategoryForBuildingHotel() {
        return hasLessOrEqualBuildingsInCategoryForHotel(false);
    }

    private boolean hasLessOrEqualBuildingsInCategoryForRemovingHotel() {
        return hasLessOrEqualBuildingsInCategoryForHotel(true);
    }

    private boolean hasLessOrEqualBuildingsInCategoryForHotel(boolean removing) {
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

    private boolean hasValidBuildingsForHotel() {
        return property.getHouseNumber() == MAX_NUMBER_OF_HOUSES && property.getHotelNumber() < MAX_NUMBER_OF_HOTEL;
    }

    private boolean hasValidBuildingsForRemovingHotel() {
        return property.getHouseNumber() == 0 && property.getHotelNumber() > 0;
    }
}
