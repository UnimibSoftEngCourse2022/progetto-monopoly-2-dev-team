package ut;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.PropertyMapper;
import model.property.PropertyCategory;
import model.property.PropertyModel;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.List;

public class PropertyMapperTest {

    private static PropertyMapper propertyMapper;
    private static List<PropertyModel> properties;

    @BeforeClass
    public static void init() throws Exception {
        ObjectMapper jacksonMapper = new ObjectMapper();
        URL jsonURL = PropertyMapperTest.class.getClassLoader().getResource("properties.json");
        if (jsonURL != null) {
            properties = jacksonMapper.readValue(new File(jsonURL.toURI()), new TypeReference<>() {
            });
            propertyMapper = new PropertyMapper(properties);
        }
    }

    @Test
    public void categoryMapperTest() {
        List<PropertyModel> categoryProperties = propertyMapper.getCategoryProperties(PropertyCategory.BROWN);

        Assert.assertNotNull(categoryProperties);

        Assert.assertEquals(2, categoryProperties.size());

        Assert.assertEquals(PropertyCategory.BROWN, categoryProperties.get(0).getCategory());
    }

}
