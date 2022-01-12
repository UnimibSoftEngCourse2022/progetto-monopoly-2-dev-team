package ut;

import controller.DiceRoller;
import manager.pricemanager.*;
import model.PropertyCategoryMapper;
import model.PropertyOwnerMapper;
import model.player.PlayerModel;
import model.property.PropertyCategory;
import model.property.PropertyModel;
import org.junit.Assert;
import org.junit.Test;
import util.Pair;

public class PriceManagerTest {
    private static PropertyCategoryMapper propertyCategoryMapper = TestUtils.getPropertyCategoryMapper();
    private static PropertyOwnerMapper propertyOwnerMapper = TestUtils.getPropertyOwnerMapper();

    @Test
    public void coloredPriceManagerNoRandomizationTest() {
        PriceManager priceManager = new ColoredPriceManager(propertyOwnerMapper, propertyCategoryMapper);
        PropertyModel property = TestUtils.getProperties().get(0);
        Assert.assertEquals(60, priceManager.getPrice(property));
        Assert.assertEquals(2, priceManager.getRent(property));
        PlayerModel playerModel = new PlayerModel("0", "name", 1000);
        for (PropertyModel categoryProperty : propertyCategoryMapper.getCategoryProperties(property.getCategory())) {
            propertyOwnerMapper.setOwner(categoryProperty, playerModel);
        }
        Assert.assertEquals(4, priceManager.getRent(property));
        property.setHouseNumber(2);
        Assert.assertEquals(30, priceManager.getRent(property));
        property.setHotelNumber(1);
        Assert.assertEquals(250, priceManager.getRent(property));
    }

    @Test
    public void railroadPriceManagerNoRandomizationTest() {
        PriceManager priceManager = new RailroadPriceManager(propertyOwnerMapper, propertyCategoryMapper);
        PropertyModel property = TestUtils
                .getPropertyCategoryMapper()
                .getCategoryProperties(PropertyCategory.RAILROAD)
                .get(0);
        Assert.assertEquals(200, priceManager.getPrice(property));
        Assert.assertEquals(25, priceManager.getRent(property));
        PlayerModel playerModel = new PlayerModel("0", "name", 1000);
        for (PropertyModel categoryProperty : propertyCategoryMapper.getCategoryProperties(property.getCategory())) {
            propertyOwnerMapper.setOwner(categoryProperty, playerModel);
        }
        Assert.assertEquals(200, priceManager.getRent(property));
    }

    @Test
    public void utilityPriceManagerNoRandomizationTest() {
        PriceManager priceManager = new UtilityPriceManager(
                propertyOwnerMapper,
                propertyCategoryMapper,
                () -> new Pair<>(5, 3)
        );
        PropertyModel property = TestUtils
                .getPropertyCategoryMapper()
                .getCategoryProperties(PropertyCategory.UTILITY)
                .get(0);
        Assert.assertEquals(150, priceManager.getPrice(property));
        Assert.assertEquals(4 * (5 + 3), priceManager.getRent(property));
        PlayerModel playerModel = new PlayerModel("0", "name", 1000);
        for (PropertyModel categoryProperty : propertyCategoryMapper.getCategoryProperties(property.getCategory())) {
            propertyOwnerMapper.setOwner(categoryProperty, playerModel);
        }
        Assert.assertEquals(10 * (5 + 3), priceManager.getRent(property));
    }

    @Test
    public void priceManagerDispatcherNoRandomizationTest() {
        PriceManager priceManager = new PriceManagerDispatcher(
                propertyOwnerMapper,
                propertyCategoryMapper,
                () -> new Pair<>(5, 3)
        );
        PropertyModel property = TestUtils
                .getPropertyCategoryMapper()
                .getCategoryProperties(PropertyCategory.UTILITY)
                .get(0);
        Assert.assertEquals(150, priceManager.getPrice(property));
        Assert.assertEquals(4 * (5 + 3), priceManager.getRent(property));
        PlayerModel playerModel = new PlayerModel("0", "name", 1000);
        for (PropertyModel categoryProperty : propertyCategoryMapper.getCategoryProperties(property.getCategory())) {
            propertyOwnerMapper.setOwner(categoryProperty, playerModel);
        }
        Assert.assertEquals(10 * (5 + 3), priceManager.getRent(property));

        property = TestUtils
                .getPropertyCategoryMapper()
                .getCategoryProperties(PropertyCategory.RAILROAD)
                .get(0);
        Assert.assertEquals(200, priceManager.getPrice(property));
        Assert.assertEquals(25, priceManager.getRent(property));
        for (PropertyModel categoryProperty : propertyCategoryMapper.getCategoryProperties(property.getCategory())) {
            propertyOwnerMapper.setOwner(categoryProperty, playerModel);
        }
        Assert.assertEquals(200, priceManager.getRent(property));

        property = TestUtils
                .getPropertyCategoryMapper()
                .getCategoryProperties(PropertyCategory.ORANGE)
                .get(0);
        Assert.assertEquals(180, priceManager.getPrice(property));
        Assert.assertEquals(14, priceManager.getRent(property));
        for (PropertyModel categoryProperty : propertyCategoryMapper.getCategoryProperties(property.getCategory())) {
            propertyOwnerMapper.setOwner(categoryProperty, playerModel);
        }
        Assert.assertEquals(28, priceManager.getRent(property));
        property.setHouseNumber(2);
        Assert.assertEquals(200, priceManager.getRent(property));
        property.setHotelNumber(1);
        Assert.assertEquals(950, priceManager.getRent(property));
        Assert.assertEquals(90, priceManager.getMortgageValue(property));
    }
}
