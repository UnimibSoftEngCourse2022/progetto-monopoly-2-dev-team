package ut;

import it.monopoly.model.property.PropertyCategory;
import it.monopoly.model.property.PropertyModel;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PropertyMapperTest extends BaseTest {


    @Test
    public void categoryMapperTest() {
        List<PropertyModel> categoryProperties = categoryMapper.getCategoryProperties(PropertyCategory.BROWN);

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
    }

}
