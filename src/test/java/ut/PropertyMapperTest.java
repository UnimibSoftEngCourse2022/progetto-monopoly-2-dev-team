package ut;

import model.PropertyMapper;
import model.property.PropertyCategory;
import model.property.PropertyModel;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PropertyMapperTest {

    private static PropertyMapper propertyMapper;

    @BeforeClass
    public static void init() {
        List<PropertyModel> testList = new ArrayList<>();
        testList.add(new PropertyModel(
                "Vicolo Stretto",
                PropertyCategory.BROWN,
                200,
                0,
                0,
                50,
                50,
                100,
                2,
                new int[]{4, 6, 8, 10},
                5));

        testList.add(new PropertyModel(
                "Vicolo Largo",
                PropertyCategory.RED,
                200,
                0,
                0,
                50,
                50,
                100,
                2,
                new int[]{4, 6, 8, 10},
                5));

        testList.add(new PropertyModel(
                "Vicolo Lungo",
                PropertyCategory.GREEN,
                2000,
                0,
                0,
                50,
                50,
                1000,
                2,
                new int[]{4, 6, 8, 10},
                5));

        propertyMapper = new PropertyMapper(testList);
    }

    @Test
    public void getPlayerPropertyTest() {
        List<PropertyModel> categoryProperties = propertyMapper.getCategoryProperties(PropertyCategory.BROWN);
        Assert.assertNotNull(categoryProperties);

        Assert.assertEquals(categoryProperties.size(), 1);

        Assert.assertEquals(categoryProperties.get(0).getHotelNumber(), 0);

        Assert.assertEquals(categoryProperties.get(0).getCategory(), PropertyCategory.BROWN);
    }

}
