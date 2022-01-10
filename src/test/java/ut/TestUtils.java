package ut;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.PropertyCategoryMapper;
import model.PropertyMapper;
import model.PropertyOwnerMapper;
import model.property.PropertyModel;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class TestUtils {
    private static PropertyMapper propertyMapper;
    private static List<PropertyModel> properties;


    private static void init() {
        ObjectMapper jacksonMapper = new ObjectMapper();
        URL jsonURL = PropertyMapperTest.class.getClassLoader().getResource("properties-test.json");
        if (jsonURL != null) {
            try {
                properties = jacksonMapper.readValue(new File(jsonURL.toURI()), new TypeReference<>() {
                });
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
                properties = Collections.emptyList();
            }
            propertyMapper = new PropertyMapper(properties);
        }
    }

    public static PropertyOwnerMapper getPropertyOwnerMapper() {
        if (propertyMapper == null) {
            init();
        }
        return propertyMapper;
    }

    public static PropertyCategoryMapper getPropertyCategoryMapper() {
        if (propertyMapper == null) {
            init();
        }
        return propertyMapper;
    }

    public static List<PropertyModel> getProperties() {
        if (properties == null) {
            init();
        }
        return properties;
    }
}
