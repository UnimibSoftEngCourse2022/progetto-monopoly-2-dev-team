package manager.pricemanager;

import controller.event.DiceRoller;
import model.PropertyCategoryMapper;
import model.PropertyOwnerMapper;
import model.player.PlayerModel;
import model.property.PropertyCategory;
import model.property.PropertyModel;
import util.Pair;

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
