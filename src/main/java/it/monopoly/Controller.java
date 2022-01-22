package it.monopoly;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.monopoly.controller.MainCommandController;
import it.monopoly.controller.TradeController;
import it.monopoly.controller.TurnController;
import it.monopoly.controller.command.CommandBuilderDispatcher;
import it.monopoly.controller.command.MainCommandBuilderDispatcher;
import it.monopoly.controller.event.EventDispatcher;
import it.monopoly.controller.event.MainEventDispatcher;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.controller.property.PropertyController;
import it.monopoly.manager.AuctionManager;
import it.monopoly.manager.player.PlayerManager;
import it.monopoly.model.PropertyMapper;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.player.ReadablePlayerModel;
import it.monopoly.model.property.PropertyModel;
import it.monopoly.view.MainView;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Controller {
    private final PlayerController playerController;
    private final PropertyController propertyController;
    private final PropertyMapper mapper;
    private final MainCommandController commandController;
    private final Map<PlayerModel, MainView> playerViewMap = new ConcurrentHashMap<>();
    private final TurnController turnController;
    private final Broadcaster broadcaster;
    private final EventDispatcher eventDispatcher;
    private boolean gameStarted = false;
    private final Logger logger = LogManager.getLogger(getClass());

    Controller() {
        List<PropertyModel> properties = Collections.emptyList();
        List<PlayerModel> players = new ArrayList<>();
        ObjectMapper jacksonMapper = new ObjectMapper();
        URL jsonURL = MainView.class.getClassLoader().getResource("properties.json");
        if (jsonURL != null) {
            try {
                properties = jacksonMapper.readValue(new File(jsonURL.toURI()), new TypeReference<>() {
                });
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
        mapper = new PropertyMapper(properties);
        playerController = new PlayerController(players, mapper);
        propertyController = new PropertyController(properties, mapper, mapper);
        TradeController tradeController = new TradeController(playerController, propertyController);
        broadcaster = new Broadcaster(playerController);
        eventDispatcher = new MainEventDispatcher(broadcaster, tradeController);
        CommandBuilderDispatcher builderDispatcher = new MainCommandBuilderDispatcher(propertyController, playerController, tradeController, eventDispatcher);
        commandController = new MainCommandController(builderDispatcher);
        turnController = new TurnController(playerController);
    }

    public synchronized PlayerModel setupPlayer(MainView view) {
        PlayerModel player = new PlayerModel(RandomStringUtils.randomAlphanumeric(6), "nome");
        playerViewMap.put(player, view);
        synchronized (propertyController.getModels()) {
            playerController.addPlayer(player, 1000);
        }
        PlayerManager manager = playerController.getManager(player);
        mapper.register(manager);
        manager.register(view.getObserver(ReadablePlayerModel.class));
        broadcaster.registerForAuction(view.getConsumer(AuctionManager.class));
        manager.register(turnController);
        LogManager.getLogger(getClass()).info("Adding new player {}#{}", player.getName(), player.getId());
        return player;
    }

    public void startGame() {
        synchronized (playerController.getModels()) {
            if (!isGameStarted() && !playerController.getModels().isEmpty()) {
                logger.info("Starting game");
                turnController.start();
                gameStarted = true;
            }
        }
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public List<PropertyModel> getProperties() {
        return propertyController.getModels();
    }

    public List<PlayerModel> getPlayers() {
        return playerController.getModels();
    }

    public ReadablePlayerModel getReadablePlayer(PlayerModel playerModel) {
        return playerController.getManager(playerModel).getReadable();
    }

    public PropertyController getPropertyController() {
        return propertyController;
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public MainCommandController getCommandController() {
        return commandController;
    }

    public Broadcaster getBroadcaster() {
        return broadcaster;
    }

    public EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    public void addProperty(PlayerModel player) {
        List<PropertyModel> models = propertyController.getModels();
        int random = new Random().nextInt(models.size());
        PropertyModel property = models.get(random);
        LogManager.getLogger(getClass()).info("New property ({}) {} to player {}", random, property, player.getId());
        propertyController.getManager(property).setOwner(player);
    }

    public void closePlayerSession(PlayerModel player) {
        synchronized (playerController.getModels()) {

            logger.info("Removing player {}#{}", player.getName(), player.getId());

            PlayerManager manager = playerController.getManager(player);
            MainView view = playerViewMap.get(player);

            manager.deregister(view.getObserver(ReadablePlayerModel.class));
            broadcaster.deregisterForAuction(view.getConsumer(AuctionManager.class));
            manager.setDiceRolled();
            manager.endTurn();
            mapper.deregister(manager);
            mapper.removeAllPlayerProperties(player);

            playerController.removePlayer(player);
            manager.deregister(turnController);
            playerViewMap.remove(player);


            if (playerController.getModels().isEmpty()) {
                stopGame();
            }
        }
    }

    public void stopGame() {
        logger.info("Stopping game");
        turnController.stop();
        gameStarted = false;
    }
}
