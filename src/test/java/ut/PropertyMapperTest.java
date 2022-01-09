package ut;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.PropertyCategoryMapper;
import model.PropertyMapper;
import model.PropertyOwnerMapper;
import model.property.PropertyCategory;
import model.property.PropertyModel;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.net.URL;
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
    }

}
