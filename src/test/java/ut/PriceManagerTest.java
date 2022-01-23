package ut;

import it.monopoly.manager.pricemanager.*;
import it.monopoly.model.PropertyCategoryMapper;
import it.monopoly.model.PropertyOwnerMapper;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyCategory;
import it.monopoly.model.property.PropertyModel;
import it.monopoly.util.Pair;
import org.junit.Assert;
import org.junit.Test;

public class PriceManagerTest {
    private static PropertyCategoryMapper propertyCategoryMapper = TestUtils.getPropertyCategoryMapper();
    private static PropertyOwnerMapper propertyOwnerMapper = TestUtils.getPropertyOwnerMapper();

    @Test
    public void coloredPriceManagerNoRandomizationTest() {
        PropertyModel property = TestUtils.getProperties().get(0);
        PriceManager priceManager = new ColoredPriceManager(property, propertyOwnerMapper, propertyCategoryMapper);
        Assert.assertEquals(60, priceManager.getPrice());
        Assert.assertEquals(2, priceManager.getRent());
        PlayerModel playerModel = new PlayerModel("0", "name");
        for (PropertyModel categoryProperty : propertyCategoryMapper.getCategoryProperties(property.getCategory())) {
            propertyOwnerMapper.setOwner(categoryProperty, playerModel);
        }
        Assert.assertEquals(4, priceManager.getRent());
        property.setHouseNumber(2);
        Assert.assertEquals(30, priceManager.getRent());
        property.setHotelNumber(1);
        Assert.assertEquals(250, priceManager.getRent());
    }

    @Test
    public void railroadPriceManagerNoRandomizationTest() {
        PropertyModel property = TestUtils
                .getPropertyCategoryMapper()
                .getCategoryProperties(PropertyCategory.RAILROAD)
                .get(0);
        PriceManager priceManager = new RailroadPriceManager(property, propertyOwnerMapper, propertyCategoryMapper);
        Assert.assertEquals(200, priceManager.getPrice());
        Assert.assertEquals(25, priceManager.getRent());
        PlayerModel playerModel = new PlayerModel("0", "name");
        for (PropertyModel categoryProperty : propertyCategoryMapper.getCategoryProperties(property.getCategory())) {
            propertyOwnerMapper.setOwner(categoryProperty, playerModel);
        }
        Assert.assertEquals(200, priceManager.getRent());
    }

    @Test
    public void utilityPriceManagerNoRandomizationTest() {
        PropertyModel property = TestUtils
                .getPropertyCategoryMapper()
                .getCategoryProperties(PropertyCategory.UTILITY)
                .get(0);
        PriceManager priceManager = new UtilityPriceManager(
                property,
                propertyOwnerMapper,
                propertyCategoryMapper,
                () -> new Pair<>(5, 3)
        );
        Assert.assertEquals(150, priceManager.getPrice());
        Assert.assertEquals(4 * (5 + 3), priceManager.getRent());
        PlayerModel playerModel = new PlayerModel("0", "name");
        for (PropertyModel categoryProperty : propertyCategoryMapper.getCategoryProperties(property.getCategory())) {
            propertyOwnerMapper.setOwner(categoryProperty, playerModel);
        }
        Assert.assertEquals(10 * (5 + 3), priceManager.getRent());
    }

    @Test
    public void priceManagerDispatcherNoRandomizationTest() {
        PropertyModel property = TestUtils
                .getPropertyCategoryMapper()
                .getCategoryProperties(PropertyCategory.UTILITY)
                .get(0);
        PriceManager priceManager = new PriceManagerDispatcher(
                propertyOwnerMapper,
                propertyCategoryMapper,
                () -> new Pair<>(5, 3)
        ).getPriceManager(property);
        Assert.assertTrue(priceManager instanceof UtilityPriceManager);

        property = TestUtils
                .getPropertyCategoryMapper()
                .getCategoryProperties(PropertyCategory.RAILROAD)
                .get(0);
        priceManager = new PriceManagerDispatcher(
                propertyOwnerMapper,
                propertyCategoryMapper,
                () -> new Pair<>(5, 3)
        ).getPriceManager(property);

        Assert.assertTrue(priceManager instanceof RailroadPriceManager);

        property = TestUtils
                .getPropertyCategoryMapper()
                .getCategoryProperties(PropertyCategory.GREEN)
                .get(0);
        priceManager = new PriceManagerDispatcher(
                propertyOwnerMapper,
                propertyCategoryMapper,
                () -> new Pair<>(5, 3)
        ).getPriceManager(property);

        Assert.assertTrue(priceManager instanceof ColoredPriceManager);
    }
}
