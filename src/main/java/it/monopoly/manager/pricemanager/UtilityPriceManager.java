package it.monopoly.manager.pricemanager;

import it.monopoly.controller.event.DiceRoller;
import it.monopoly.model.PropertyCategoryMapper;
import it.monopoly.model.PropertyOwnerMapper;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyCategory;
import it.monopoly.model.property.PropertyModel;
import it.monopoly.util.Pair;

public class UtilityPriceManager extends PriceManager {

    //TODO Add randomization logic
    private DiceRoller diceRoller;

    public UtilityPriceManager(PropertyOwnerMapper propertyOwnerMapper, PropertyCategoryMapper propertyCategoryMapper, DiceRoller diceRoller) {
        super(propertyOwnerMapper, propertyCategoryMapper);
        this.diceRoller = diceRoller;
    }

    @Override
    public int getRent(PropertyModel property) {
        return getRentMultiplier(property) * getDiceRollValue();
    }

    private int getRentMultiplier(PropertyModel property) {
        PlayerModel player = propertyOwnerMapper.getOwner(property);
        int ownedUtilities = 1;
        if (player != null) {
            ownedUtilities = (int) propertyOwnerMapper.getPlayerProperties(player)
                    .stream()
                    .filter(propertyModel -> PropertyCategory.UTILITY.equals(propertyModel.getCategory()))
                    .count();
        }
        return property.getRentValue()[ownedUtilities - 1];
    }

    private int getDiceRollValue() {
        Pair<Integer, Integer> roll = diceRoller.rollDice();
        return roll.getFirst() + roll.getSecond();
    }
}
