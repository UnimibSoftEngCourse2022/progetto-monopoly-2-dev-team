package ut;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.monopoly.controller.TradeController;
import it.monopoly.controller.command.CommandBuilderDispatcher;
import it.monopoly.controller.command.MainCommandBuilderDispatcher;
import it.monopoly.controller.event.callback.BuyOrAuctionCallback;
import it.monopoly.controller.event.callback.DiceRollEventCallback;
import it.monopoly.controller.event.DiceRoller;
import it.monopoly.controller.event.EventDispatcher;
import it.monopoly.controller.event.callback.UsePointsCallback;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.controller.property.PropertyController;
import it.monopoly.manager.AbstractOfferManager;
import it.monopoly.manager.pricemanager.PriceManagerDispatcher;
import it.monopoly.model.PropertyCategoryMapper;
import it.monopoly.model.PropertyMapper;
import it.monopoly.model.PropertyOwnerMapper;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyModel;
import it.monopoly.model.property.ReadablePropertyModel;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseTest {
    protected static PropertyMapper propertyMapper;
    protected static PropertyOwnerMapper ownerMapper;
    protected static PropertyCategoryMapper categoryMapper;

    protected static List<PropertyModel> properties;
    protected static List<PlayerModel> players;

    protected static PlayerController playerController;
    protected static PropertyController propertyController;
    protected static CommandBuilderDispatcher commandBuilderDispatcher;
    protected static DiceRoller diceRoller;
    protected static TradeController tradeController;
    protected static EventDispatcher eventDispatcher;

    public static void resetPlayers() {
        players = new ArrayList<>();
        players.add(new PlayerModel("1", "name1"));
        players.add(new PlayerModel("2", "name2"));
        players.add(new PlayerModel("3", "name3"));
        players.add(new PlayerModel("4", "name4"));
    }

    @Before
    public void init() {
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
            ownerMapper = propertyMapper;
            categoryMapper = propertyMapper;
        }
        if (players == null) {
            resetPlayers();
        }

        eventDispatcher = new EventDispatcher() {
            @Override
            public DiceRoller diceRollEvent() {
                return diceRoller;
            }

            @Override
            public DiceRoller diceRollEvent(DiceRollEventCallback callback) {
                return diceRoller;
            }

            @Override
            public void startOffer(AbstractOfferManager offerManager) {
            }

            @Override
            public void buyOrAuction(PlayerModel player, ReadablePropertyModel propertyModel, BuyOrAuctionCallback callback) {
            }

            @Override
            public void useLoyaltyPoints(PlayerModel player, UsePointsCallback usePointsCallback) {
                usePointsCallback.use(Integer.parseInt(playerController.getManager(player).getLoyaltyProgram().getValue()));
            }

            @Override
            public void showDialog(PlayerModel player, String message) {

            }

            @Override
            public void sendMessage(String message) {
            }
        };
        PriceManagerDispatcher priceManagerDispatcher = new PriceManagerDispatcher(null, ownerMapper, categoryMapper, eventDispatcher.diceRollEvent());
        playerController = new PlayerController(players, ownerMapper);
        propertyController = new PropertyController(properties, priceManagerDispatcher, ownerMapper, categoryMapper);
        tradeController = new TradeController(playerController, propertyController);

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
}
