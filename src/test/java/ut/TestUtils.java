package ut;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.monopoly.model.PropertyCategoryMapper;
import it.monopoly.model.PropertyMapper;
import it.monopoly.model.PropertyOwnerMapper;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyModel;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
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

    public static List<PlayerModel> getPlayers() {
        List<PlayerModel> players = new ArrayList<>();
        players.add(new PlayerModel("1", "name1"));
        players.add(new PlayerModel("2", "name2"));
        players.add(new PlayerModel("3", "name3"));
        players.add(new PlayerModel("4", "name4"));
        return players;
    }
}
