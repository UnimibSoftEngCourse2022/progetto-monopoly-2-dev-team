package it.monopoly.manager.pricemanager;

import it.monopoly.controller.event.DiceRoller;
import it.monopoly.manager.randomizer.PropertyRandomizerManager;
import it.monopoly.model.PropertyCategoryMapper;
import it.monopoly.model.PropertyOwnerMapper;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyCategory;
import it.monopoly.model.property.PropertyModel;
import it.monopoly.util.Pair;

public class UtilityPriceManager extends PriceManager {
    private final DiceRoller diceRoller;

    public UtilityPriceManager(PropertyModel property,
                               PropertyRandomizerManager propertyRandomizerManager,
                               PropertyOwnerMapper propertyOwnerMapper,
                               PropertyCategoryMapper propertyCategoryMapper,
                               DiceRoller diceRoller) {
        super(property, propertyRandomizerManager, propertyOwnerMapper, propertyCategoryMapper);
        this.diceRoller = diceRoller;
    }

    @Override
    protected int getCleanRent() {
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
        if (diceRoller == null) {
            return 1;
        }
        Pair<Integer, Integer> roll = diceRoller.rollDice();
        return roll.getFirst() + roll.getSecond();
    }

    @Override
    public int getRent() {
        return super.getRent() * getDiceRollValue();
    }

    @Override
    public String getRentString() {
        return String.valueOf(super.getRent()) + " x dice roll";
    }
}
