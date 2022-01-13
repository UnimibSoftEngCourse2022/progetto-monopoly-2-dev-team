package ut;

import manager.pricemanager.PriceManager;
import manager.property.PropertyManager;
import model.PropertyCategoryMapper;
import model.PropertyOwnerMapper;
import model.player.PlayerModel;
import model.property.PropertyCategory;
import model.property.PropertyModel;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PropertyManagerTest {
    PropertyOwnerMapper ownerMapper = TestUtils.getPropertyOwnerMapper();
    PropertyCategoryMapper categoryMapper = TestUtils.getPropertyCategoryMapper();

    @Test
    public void propertyManagerBuildingTest() {
        PropertyManager propertyManager = new PropertyManager(
                ownerMapper,
                categoryMapper,
                new PriceManager(ownerMapper, categoryMapper) {
                    @Override
                    public int getPrice(PropertyModel property) {
                        return 0;
                    }

                    @Override
                    public int getRent(PropertyModel property) {
                        return 0;
                    }
                }
        );

        //RAILROADS
        List<PropertyModel> properties = categoryMapper.getCategoryProperties(PropertyCategory.RAILROAD);
        PropertyModel firstProperty = properties.get(0);
        for (PropertyModel property : properties) {
            ownerMapper.removeOwner(property);
        }
        Assert.assertFalse(propertyManager.buildHouse(firstProperty));

        PlayerModel player = new PlayerModel("id", "name", 1000);
        for (PropertyModel property : properties) {
            ownerMapper.setOwner(property, player);
        }
        Assert.assertFalse(propertyManager.canImproveHouse(firstProperty));
        Assert.assertFalse(propertyManager.canImproveHotel(firstProperty));

        //PINK
        properties = categoryMapper.getCategoryProperties(PropertyCategory.PINK);
        firstProperty = properties.get(0);

        for (PropertyModel property : properties) {
            ownerMapper.removeOwner(property);
        }
        Assert.assertFalse(propertyManager.buildHouse(firstProperty));

        for (PropertyModel property : properties) {
            ownerMapper.setOwner(property, player);
        }
        Assert.assertTrue(propertyManager.buildHouse(firstProperty));
        Assert.assertFalse(propertyManager.buildHouse(firstProperty));
        Assert.assertFalse(propertyManager.buildHotel(firstProperty));
        Assert.assertFalse(propertyManager.canSell(firstProperty));
        Assert.assertFalse(propertyManager.canMortgage(firstProperty));

        for (PropertyModel property : properties) {
            property.setHouseNumber(0);
            property.setHotelNumber(0);
        }
        Assert.assertTrue(propertyManager.canSell(firstProperty));
        Assert.assertTrue(propertyManager.canCollectRent(firstProperty));
        Assert.assertTrue(propertyManager.canMortgage(firstProperty));
        Assert.assertTrue(propertyManager.mortgage(firstProperty));
        Assert.assertFalse(propertyManager.canCollectRent(firstProperty));
        Assert.assertFalse(propertyManager.buildHouse(firstProperty));
        Assert.assertFalse(propertyManager.buildHotel(firstProperty));
        Assert.assertFalse(propertyManager.canMortgage(firstProperty));
        Assert.assertTrue(propertyManager.canLiftMortgage(firstProperty));
        Assert.assertTrue(propertyManager.liftMortgage(firstProperty));

        do {
            for (PropertyModel property : properties) {
                Assert.assertTrue(propertyManager.buildHouse(property));
            }
        } while (propertyManager.canImproveHouse(properties.get(properties.size() - 1)));
        Assert.assertFalse(propertyManager.buildHouse(firstProperty));
        Assert.assertFalse(propertyManager.canImproveHouse(firstProperty));
        Assert.assertTrue(propertyManager.canImproveHotel(firstProperty));
        Assert.assertTrue(propertyManager.buildHotel(firstProperty));
        Assert.assertFalse(propertyManager.canImproveHotel(firstProperty));
        Assert.assertTrue(propertyManager.buildHotel(properties.get(1)));
        Assert.assertTrue(propertyManager.canRemoveHotel(firstProperty));
        Assert.assertTrue(propertyManager.removeHotel(firstProperty));
        Assert.assertTrue(propertyManager.removeHotel(properties.get(1)));
        Assert.assertTrue(propertyManager.canRemoveHouse(firstProperty));

        do {
            for (PropertyModel property : properties) {
                Assert.assertTrue(propertyManager.removeHouse(property));
                if (property != properties.get(properties.size() - 1)) {
                    Assert.assertFalse(propertyManager.removeHouse(property));
                }
            }
        } while (propertyManager.canRemoveHouse(properties.get(properties.size() - 1)));
        for (PropertyModel property : properties) {
            Assert.assertEquals(0, property.getHouseNumber());
            Assert.assertEquals(0, property.getHotelNumber());
        }
    }
}
