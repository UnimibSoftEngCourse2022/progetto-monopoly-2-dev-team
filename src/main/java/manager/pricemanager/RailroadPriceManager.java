package manager.pricemanager;

import model.PropertyCategoryMapper;
import model.PropertyOwnerMapper;
import model.player.PlayerModel;
import model.property.PropertyCategory;
import model.property.PropertyModel;

public class RailroadPriceManager extends PriceManager {

    //TODO Add randomization logic

    public RailroadPriceManager(PropertyOwnerMapper propertyOwnerMapper, PropertyCategoryMapper propertyCategoryMapper) {
        super(propertyOwnerMapper, propertyCategoryMapper);
    }

    @Override
    public int getRent(PropertyModel property) {
        return getCleanRent(property);
    }

    private int getCleanRent(PropertyModel property) {
        PlayerModel player = propertyOwnerMapper.getOwner(property);
        int ownedRailroads = 1;
        if (player != null) {
            ownedRailroads = (int) propertyOwnerMapper.getPlayerProperties(player)
                    .stream()
                    .filter(propertyModel -> PropertyCategory.RAILROAD.equals(propertyModel.getCategory()))
                    .count();
        }
        return property.getRentValue()[ownedRailroads - 1];
    }
}