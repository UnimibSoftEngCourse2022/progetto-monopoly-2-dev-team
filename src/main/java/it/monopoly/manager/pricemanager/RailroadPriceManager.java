package it.monopoly.manager.pricemanager;

import it.monopoly.model.PropertyCategoryMapper;
import it.monopoly.model.PropertyOwnerMapper;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyCategory;
import it.monopoly.model.property.PropertyModel;

public class RailroadPriceManager extends PriceManager {

    //TODO Add randomization logic

    public RailroadPriceManager(PropertyModel property, PropertyOwnerMapper propertyOwnerMapper, PropertyCategoryMapper propertyCategoryMapper) {
        super(property, propertyOwnerMapper, propertyCategoryMapper);
    }

    @Override
    public int getRent() {
        return getCleanRent();
    }

    private int getCleanRent() {
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