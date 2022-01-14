package ut;

import manager.pricemanager.ColoredPriceManager;
import manager.pricemanager.PriceManager;
import model.PropertyCategoryMapper;
import model.PropertyOwnerMapper;
import model.player.PlayerModel;
import model.property.PropertyModel;
import org.junit.Assert;
import org.junit.Test;

public class PriceManagerTest {
    private static PropertyCategoryMapper propertyCategoryMapper = TestUtils.getPropertyCategoryMapper();
    private static PropertyOwnerMapper propertyOwnerMapper = TestUtils.getPropertyOwnerMapper();

    @Test
    public void coloredPriceManagerTest() {
        PriceManager priceManager = new ColoredPriceManager(propertyOwnerMapper, propertyCategoryMapper);
        PropertyModel property = TestUtils.getProperties().get(0);
        Assert.assertEquals(60, priceManager.getPrice(property));
        Assert.assertEquals(2, priceManager.getRent(property));
        PlayerModel playerModel = new PlayerModel("0", "name");
        for (PropertyModel categoryProperty : propertyCategoryMapper.getCategoryProperties(property.getCategory())) {
            propertyOwnerMapper.setOwner(categoryProperty, playerModel);
        }
        Assert.assertEquals(4, priceManager.getRent(property));
        property.setHouseNumber(2);
        Assert.assertEquals(30, priceManager.getRent(property));
        property.setHotelNumber(1);
        Assert.assertEquals(250, priceManager.getRent(property));
    }
}
