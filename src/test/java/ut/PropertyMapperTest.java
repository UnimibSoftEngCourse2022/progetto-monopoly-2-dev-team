package ut;

import it.monopoly.model.PropertyCategoryMapper;
import it.monopoly.model.PropertyOwnerMapper;
import it.monopoly.model.property.PropertyCategory;
import it.monopoly.model.property.PropertyModel;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PropertyMapperTest {

    private static PropertyCategoryMapper propertyCategoryMapper = TestUtils.getPropertyCategoryMapper();
    private static PropertyOwnerMapper propertyOwnerMapper = TestUtils.getPropertyOwnerMapper();

    @Test
    public void categoryMapperTest() {
        List<PropertyModel> categoryProperties = propertyCategoryMapper.getCategoryProperties(PropertyCategory.BROWN);

        Assert.assertNotNull(categoryProperties);
        Assert.assertEquals(2, categoryProperties.size());

        PropertyModel model = categoryProperties.get(0);


        Assert.assertEquals(PropertyCategory.BROWN, model.getCategory());
        Assert.assertTrue(model.getHouseRent(3) < model.getHouseRent(4));
        Assert.assertEquals("Mediterranean Avenue", model.getName());
        Assert.assertEquals(60, model.getPrice());
        Assert.assertEquals(50, model.getHousePrice());
        Assert.assertEquals(50, model.getHotelPrice());
        Assert.assertEquals(30, model.getMortgageValue());


        Assert.assertEquals(250, model.getHotelRent());
        Assert.assertTrue(model.isImprovable());
        model.setHouseNumber(1);
        Assert.assertEquals(1, model.getHouseNumber());
        Assert.assertEquals(10, model.getHouseRent());
        model.setHouseNumber(4);
        Assert.assertEquals(4, model.getHouseNumber());
        Assert.assertEquals(160, model.getHouseRent());
        /*
        it.monopoly.model.setHotelNumber(1);
        Assert.assertEquals(0, it.monopoly.model.getHouseNumber());
        Assert.assertEquals(1, it.monopoly.model.getHotelNumber());
        Assert.assertEquals(250, it.monopoly.model.getHotelNumber());
         */
    }

}
