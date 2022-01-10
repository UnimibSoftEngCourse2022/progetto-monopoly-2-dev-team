package manager.pricemanager;

import model.PropertyCategoryMapper;
import model.PropertyOwnerMapper;
import model.player.PlayerModel;
import model.property.PropertyCategory;
import model.property.PropertyModel;

public class UtilityPriceManager extends PriceManager {

    //TODO Add randomization logic

    protected UtilityPriceManager(PropertyOwnerMapper propertyOwnerMapper, PropertyCategoryMapper propertyCategoryMapper) {
        super(propertyOwnerMapper, propertyCategoryMapper);
    }

    @Override
    public int getPrice(PropertyModel property) {
        return property.getPrice();
    }

    @Override
    public int getRent(PropertyModel property) {
        return getRentMultiplier(property) * getDiceRollValue();
    }

    private int getRentMultiplier(PropertyModel property) {
        PlayerModel player = propertyOwnerMapper.getOwner(property);
        int ownedUtilities = 0;
        if (player != null) {
            ownedUtilities = (int) propertyOwnerMapper.getPlayerProperties(player)
                    .stream()
                    .filter(propertyModel -> PropertyCategory.UTILITY.equals(propertyModel.getCategory()))
                    .count();
        }
        return property.getRentValue()[ownedUtilities - 1];
    }

    private int getDiceRollValue() {
        return 6; //TODO implement dice roll
    }
}
