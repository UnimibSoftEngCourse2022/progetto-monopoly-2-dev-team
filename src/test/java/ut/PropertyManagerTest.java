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

import java.util.ArrayList;
import java.util.List;

public class PropertyManagerTest {
    private static final PropertyOwnerMapper ownerMapper = TestUtils.getPropertyOwnerMapper();
    private static final PropertyCategoryMapper categoryMapper = TestUtils.getPropertyCategoryMapper();

    @Test
    public void propertyManagerBuildingTestNotImprovable() {
        //RAILROADS
        final List<PropertyModel> properties = categoryMapper.getCategoryProperties(PropertyCategory.RAILROAD);
        PropertyManager propertyManager = getPropertyManager(properties.get(0));
        for (PropertyModel property : properties) {
            ownerMapper.removeOwner(property);
        }
        Assert.assertFalse(propertyManager.buildHouse());

        PlayerModel player = getPlayer();
        for (PropertyModel property : properties) {
            ownerMapper.setOwner(property, player);
        }
        Assert.assertFalse(propertyManager.canImproveHouse());
        Assert.assertFalse(propertyManager.canImproveHotel());
    }

    @Test
    public void propertyManagerBuildingTestPink() {
        //PINK
        List<PropertyModel> properties = categoryMapper.getCategoryProperties(PropertyCategory.PINK);
        PropertyManager propertyManager = getPropertyManager(properties.get(0));
        PlayerModel player = getPlayer();

        for (PropertyModel property : properties) {
            ownerMapper.removeOwner(property);
        }
        Assert.assertFalse(propertyManager.buildHouse());

        for (PropertyModel property : properties) {
            ownerMapper.setOwner(property, player);
        }
        Assert.assertTrue(propertyManager.buildHouse());
        Assert.assertFalse(propertyManager.buildHouse());
        Assert.assertFalse(propertyManager.buildHotel());
        Assert.assertFalse(propertyManager.canSell());
        Assert.assertFalse(propertyManager.canMortgage());

        for (PropertyModel property : properties) {
            property.setHouseNumber(0);
            property.setHotelNumber(0);
        }
        Assert.assertTrue(propertyManager.canSell());
        Assert.assertTrue(propertyManager.canCollectRent());
        Assert.assertTrue(propertyManager.canMortgage());
        Assert.assertTrue(propertyManager.mortgage());
        Assert.assertFalse(propertyManager.canCollectRent());
        Assert.assertFalse(propertyManager.buildHouse());
        Assert.assertFalse(propertyManager.buildHotel());
        Assert.assertFalse(propertyManager.canMortgage());
        Assert.assertTrue(propertyManager.canLiftMortgage());
        Assert.assertTrue(propertyManager.liftMortgage());

        List<PropertyManager> managers = new ArrayList<>();
        for (PropertyModel property : properties) {
            managers.add(getPropertyManager(property));
        }

        do {
            for (PropertyManager manager : managers) {
                Assert.assertTrue(manager.buildHouse());
            }
        } while (managers.get(managers.size() - 1).canImproveHouse());
        Assert.assertFalse(propertyManager.buildHouse());
        Assert.assertFalse(propertyManager.canImproveHouse());
        Assert.assertTrue(propertyManager.canImproveHotel());
        Assert.assertTrue(propertyManager.buildHotel());
        Assert.assertFalse(propertyManager.canImproveHotel());
        Assert.assertTrue(managers.get(1).buildHotel());
        Assert.assertTrue(propertyManager.canRemoveHotel());
        Assert.assertTrue(propertyManager.removeHotel());
        Assert.assertTrue(managers.get(1).removeHotel());
        Assert.assertTrue(propertyManager.canRemoveHouse());

        do {
            for (PropertyManager manager : managers) {
                Assert.assertTrue(manager.removeHouse());
                if (manager != managers.get(properties.size() - 1)) {
                    Assert.assertFalse(manager.removeHouse());
                }
            }
        } while (managers.get(properties.size() - 1).canRemoveHouse());
        for (PropertyModel property : properties) {
            Assert.assertEquals(0, property.getHouseNumber());
            Assert.assertEquals(0, property.getHotelNumber());
        }
    }

    public static PropertyManager getPropertyManager(PropertyModel propertyModel) {
        return new PropertyManager(
                propertyModel,
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
    }

    public static PlayerModel getPlayer() {
        return new PlayerModel("id", "name", 1000);
    }
}
