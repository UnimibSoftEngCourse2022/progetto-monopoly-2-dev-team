package manager.property;

import manager.Manager;
import manager.pricemanager.PriceManager;
import manager.pricemanager.PriceManagerDispatcher;
import model.PropertyCategoryMapper;
import model.PropertyOwnerMapper;
import model.player.PlayerModel;
import model.property.PropertyModel;

import java.util.List;
import java.util.stream.Stream;

public class PropertyManager extends Manager<PropertyModel> {
    private PropertyOwnerMapper ownerMapper;
    private PropertyCategoryMapper categoryMapper;
    private PriceManager priceManager;
    private static final int MAX_NUMBER_OF_HOUSES_IN_GAME = 32; //TODO Implement max number of houses/hotel in game
    private static final int MAX_NUMBER_OF_HOTEL_IN_GAME = 12;
    private static final int MAX_NUMBER_OF_HOUSES = 4;
    private static final int MAX_NUMBER_OF_HOTEL = 1;

    public PropertyManager(PropertyModel property, PropertyOwnerMapper ownerMapper, PropertyCategoryMapper categoryMapper) {
        super(property);
        this.ownerMapper = ownerMapper;
        this.categoryMapper = categoryMapper;
        this.priceManager =
                new PriceManagerDispatcher(ownerMapper, categoryMapper, null) //TODO DiceRoller
                        .getPriceManager(property);
    }

    public boolean buildHouse() {
        if (model != null && canImproveHouse()) {
            model.setHouseNumber(
                    model.getHouseNumber() + 1
            );
            return true;
        }
        return false;
    }

    public boolean buildHotel() {
        if (model != null && canImproveHotel()) {
            model.setHotelNumber(1);
            model.setHouseNumber(0);
            return true;
        }
        return false;
    }

    public boolean removeHouse() {
        if (model != null && canRemoveHouse()) {
            model.setHouseNumber(
                    model.getHouseNumber() - 1
            );
            return true;
        }
        return false;
    }

    public boolean removeHotel() {
        if (model != null && canRemoveHotel()) {
            model.setHotelNumber(0);
            model.setHouseNumber(4);
            return true;
        }
        return false;
    }

    public boolean mortgage() {
        if (!canMortgage()) {
            return false;
        }
        model.setMortgaged(true);
        return true;
    }

    public boolean liftMortgage() {
        if (!canLiftMortgage()) {
            return false;
        }
        model.setMortgaged(false);
        return true;
    }

    public boolean canImproveHouse() {
        return model != null &&
                hasRightCategory() &&
                hasValidBuildingsForHouse() &&
                ownerHasEveryPropertyInCategoryAndUnmortgaged() &&
                hasLessOrEqualBuildingsInCategoryForBuildingHouse();
    }

    public boolean canImproveHotel() {
        return model != null &&
                hasRightCategory() &&
                hasValidBuildingsForHotel() &&
                ownerHasEveryPropertyInCategoryAndUnmortgaged() &&
                hasLessOrEqualBuildingsInCategoryForBuildingHotel();
    }

    public boolean canRemoveHouse() {
        return model != null &&
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
        return !model.isMortgaged() && canSell();
    }

    public boolean canSell() {
        return zeroBuildingsInCategory();
    }

    public boolean canCollectRent() {
        return !model.isMortgaged() && ownerMapper.getOwner(model) != null;
    }

    public boolean canLiftMortgage() {
        return model.isMortgaged();
    }

    public void setOwner(PlayerModel player) {
        ownerMapper.setOwner(model, player);
    }

    public PlayerModel removeOwner() {
        return ownerMapper.removeOwner(model);
    }

    public PlayerModel getOwner() {
        return ownerMapper.getOwner(model);
    }

    public PriceManager getPriceManager() {
        return priceManager;
    }

    //PRIVATE METHODS

    private boolean hasRightCategory() {
        return model.isImprovable() && !model.isMortgaged();
    }

    private boolean ownerHasEveryPropertyInCategoryAndUnmortgaged() {
        List<PropertyModel> categoryProperties = categoryMapper
                .getCategoryProperties(model.getCategory());
        PlayerModel owner = ownerMapper.getOwner(model);
        if (owner == null) {
            return false;
        }
        Stream<PropertyModel> ownerProperties = categoryProperties
                .stream()
                .filter(
                        propertyModel -> !model.isMortgaged() && owner.equals(ownerMapper.getOwner(propertyModel))
                );
        return categoryProperties.size() == ownerProperties.count();
    }

    private boolean zeroBuildingsInCategory() {
        boolean valid = true;
        List<PropertyModel> categoryProperties = categoryMapper
                .getCategoryProperties(model.getCategory());
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
                .getCategoryProperties(model.getCategory());
        for (PropertyModel categoryProperty : categoryProperties) {
            if (removing) {
                valid &= model.getHouseNumber() >= categoryProperty.getHouseNumber();
            } else {
                valid &= model.getHouseNumber() <= categoryProperty.getHouseNumber();
            }
        }
        return valid;
    }

    private boolean hasValidBuildingsForHouse() {
        return model.getHotelNumber() == 0 && model.getHouseNumber() < MAX_NUMBER_OF_HOUSES;
    }

    private boolean hasValidBuildingsForRemovingHouse() {
        return model.getHotelNumber() == 0 && model.getHouseNumber() > 0;
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
                .getCategoryProperties(model.getCategory());
        for (PropertyModel categoryProperty : categoryProperties) {
            if (removing) {
                valid &= categoryProperty.getHouseNumber() == MAX_NUMBER_OF_HOUSES ||
                        model.getHotelNumber() == categoryProperty.getHotelNumber();
            } else {
                valid &= categoryProperty.getHouseNumber() == MAX_NUMBER_OF_HOUSES ||
                        model.getHotelNumber() <= categoryProperty.getHotelNumber();
            }
        }
        return valid;
    }

    private boolean hasValidBuildingsForHotel() {
        return model.getHouseNumber() == MAX_NUMBER_OF_HOUSES && model.getHotelNumber() < MAX_NUMBER_OF_HOTEL;
    }

    private boolean hasValidBuildingsForRemovingHotel() {
        return model.getHouseNumber() == 0 && model.getHotelNumber() > 0;
    }
}
