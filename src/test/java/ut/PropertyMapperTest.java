package ut;

import model.PropertyCategoryMapper;
import model.PropertyOwnerMapper;
import model.property.PropertyCategory;
import model.property.PropertyModel;
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
        model.setHotelNumber(1);
        Assert.assertEquals(0, model.getHouseNumber());
        Assert.assertEquals(1, model.getHotelNumber());
        Assert.assertEquals(250, model.getHotelNumber());
         */
    }

}
