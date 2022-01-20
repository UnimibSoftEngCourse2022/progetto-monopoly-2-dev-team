package ut;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import controller.TradeController;
import controller.command.CommandBuilderDispatcher;
import controller.command.MainCommandBuilderDispatcher;
import controller.event.DiceRollEventCallback;
import controller.event.DiceRoller;
import controller.event.EventDispatcher;
import controller.player.PlayerController;
import controller.property.PropertyController;
import model.DrawableCardModel;
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

    protected static List<DrawableCardModel> chances;
    protected static List<DrawableCardModel> communities;
    protected static List<PropertyModel> properties;
    protected static List<PlayerModel> players;

    protected static PlayerController playerController;
    protected static PropertyController propertyController;
    protected static CommandBuilderDispatcher commandBuilderDispatcher;
    protected static DiceRoller diceRoller;
    protected static TradeController tradeController;
    protected static EventDispatcher eventDispatcher;

    @Before
    public void init() {
        ObjectMapper jacksonMapper = new ObjectMapper();

        URL jsonPropertyURL = PropertyMapperTest.class.getClassLoader().getResource("properties-test.json");
        if (jsonPropertyURL != null) {
            try {
                properties = jacksonMapper.readValue(new File(jsonPropertyURL.toURI()), new TypeReference<>() {
                });
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
                properties = Collections.emptyList();
            }
            PropertyMapper propertyMapper = new PropertyMapper(properties);
            ownerMapper = propertyMapper;
            categoryMapper = propertyMapper;
        }
        /*
        URL jsonChancesURL = getClass().getClassLoader().getResource("chances.json");
        URL jsonCommunityChestsURL = getClass().getClassLoader().getResource("communityChests.json");
        if (jsonChancesURL != null && jsonCommunityChestsURL != null) {
            try {
                chances = jacksonMapper.readValue(new File(jsonChancesURL.toURI()), new TypeReference<>() {
                });
                communities = jacksonMapper.readValue(new File(jsonCommunityChestsURL.toURI()), new TypeReference<>() {
                });
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
        */
        if (players == null) {
            resetPlayers();
        }

        playerController = new PlayerController(players, ownerMapper);
        propertyController = new PropertyController(properties, ownerMapper, categoryMapper);
        tradeController = new TradeController(playerController, propertyController);
        eventDispatcher = new EventDispatcher() {
            @Override
            public DiceRoller diceRollEvent() {
                return diceRoller;
            }

            @Override
            public DiceRoller diceRollEvent(DiceRollEventCallback callback) {
                return diceRoller;
            }
        };

        commandBuilderDispatcher = new MainCommandBuilderDispatcher(
                propertyController,
                playerController,
                tradeController,
                eventDispatcher
        );
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
