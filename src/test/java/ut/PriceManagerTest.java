package ut;

import it.monopoly.manager.pricemanager.*;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyCategory;
import it.monopoly.model.property.PropertyModel;
import it.monopoly.util.Pair;
import org.junit.Assert;
import org.junit.Test;

public class PriceManagerTest extends BaseTest {

    @Test
    public void coloredPriceManagerNoRandomizationTest() {
        PropertyModel property = properties.get(0);
        PriceManager priceManager = new ColoredPriceManager(property, null, ownerMapper, categoryMapper);
        Assert.assertEquals(60, priceManager.getPrice());
        Assert.assertEquals(2, priceManager.getRent());
        PlayerModel playerModel = new PlayerModel("0", "name");
        for (PropertyModel categoryProperty : categoryMapper.getCategoryProperties(property.getCategory())) {
            ownerMapper.setOwner(categoryProperty, playerModel);
        }
        Assert.assertEquals(4, priceManager.getRent());
        property.setHouseNumber(2);
        Assert.assertEquals(30, priceManager.getRent());
        property.setHotelNumber(1);
        Assert.assertEquals(250, priceManager.getRent());
    }

    @Test
    public void railroadPriceManagerNoRandomizationTest() {
        PropertyModel property = categoryMapper
                .getCategoryProperties(PropertyCategory.RAILROAD)
                .get(0);
        ownerMapper.removeOwner(property);
        PriceManager priceManager = new RailroadPriceManager(property,
                null,
                ownerMapper,
                categoryMapper);
        Assert.assertEquals(200, priceManager.getPrice());
        Assert.assertEquals(25, priceManager.getRent());
        PlayerModel playerModel = new PlayerModel("0", "name");
        for (PropertyModel categoryProperty : categoryMapper.getCategoryProperties(property.getCategory())) {
            ownerMapper.setOwner(categoryProperty, playerModel);
        }
        Assert.assertEquals(200, priceManager.getRent());
    }

    @Test
    public void utilityPriceManagerNoRandomizationTest() {
        PropertyModel property = categoryMapper
                .getCategoryProperties(PropertyCategory.UTILITY)
                .get(0);
        PriceManager priceManager = new UtilityPriceManager(
                property,
                null,
                ownerMapper,
                categoryMapper,
                () -> new Pair<>(5, 3)
        );
        Assert.assertEquals(150, priceManager.getPrice());
        Assert.assertEquals(4 * (5 + 3), priceManager.getRent());
        PlayerModel playerModel = new PlayerModel("0", "name");
        for (PropertyModel categoryProperty : categoryMapper.getCategoryProperties(property.getCategory())) {
            ownerMapper.setOwner(categoryProperty, playerModel);
        }
        Assert.assertEquals(10 * (5 + 3), priceManager.getRent());
    }

    @Test
    public void priceManagerDispatcherNoRandomizationTest() {
        PropertyModel property = categoryMapper
                .getCategoryProperties(PropertyCategory.UTILITY)
                .get(0);
        PriceManager priceManager = new PriceManagerDispatcher(
                null,
                ownerMapper,
                categoryMapper,
                () -> new Pair<>(5, 3)
        ).getPriceManager(property);
        Assert.assertTrue(priceManager instanceof UtilityPriceManager);

        property = categoryMapper
                .getCategoryProperties(PropertyCategory.RAILROAD)
                .get(0);
        priceManager = new PriceManagerDispatcher(
                null,
                ownerMapper,
                categoryMapper,
                () -> new Pair<>(5, 3)
        ).getPriceManager(property);

        Assert.assertTrue(priceManager instanceof RailroadPriceManager);

        property = categoryMapper
                .getCategoryProperties(PropertyCategory.GREEN)
                .get(0);
        priceManager = new PriceManagerDispatcher(
                null,
                ownerMapper,
                categoryMapper,
                () -> new Pair<>(5, 3)
        ).getPriceManager(property);

        Assert.assertTrue(priceManager instanceof ColoredPriceManager);
    }
}
