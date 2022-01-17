package ut;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.PropertyCategoryMapper;
import model.PropertyMapper;
import model.PropertyOwnerMapper;
import model.player.PlayerModel;
import model.property.PropertyModel;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseTest {
    protected static PropertyOwnerMapper ownerMapper;
    protected static PropertyCategoryMapper categoryMapper;

    protected static List<PropertyModel> properties;
    protected static List<PlayerModel> players;

    @Before
    protected void init() {
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
            PropertyMapper propertyMapper = new PropertyMapper(properties);
            ownerMapper = propertyMapper;
            categoryMapper = propertyMapper;
        }
        if(players == null) {
            resetPlayers();
        }
    }

    public void resetProperties() {
        init();
    }

    public static void resetPlayers() {
        players = new ArrayList<>();
        players.add(new PlayerModel("1", "name1"));
        players.add(new PlayerModel("2", "name2"));
        players.add(new PlayerModel("3", "name3"));
        players.add(new PlayerModel("4", "name4"));
    }
}
