package it.monopoly.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.monopoly.Broadcaster;
import it.monopoly.controller.board.Board;
import it.monopoly.controller.command.CommandBuilderDispatcher;
import it.monopoly.controller.command.MainCommandBuilderDispatcher;
import it.monopoly.controller.event.EventDispatcher;
import it.monopoly.controller.event.MainEventDispatcher;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.controller.property.PropertyController;
import it.monopoly.manager.AuctionManager;
import it.monopoly.manager.SellManager;
import it.monopoly.manager.player.PlayerManager;
import it.monopoly.manager.pricemanager.PriceManagerDispatcher;
import it.monopoly.manager.randomizer.RandomizationManager;
import it.monopoly.model.Configuration;
import it.monopoly.model.PropertyMapper;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.player.ReadablePlayerModel;
import it.monopoly.model.property.PropertyModel;
import it.monopoly.model.property.ReadablePropertyModel;
import it.monopoly.view.MainView;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

@Component
public class Controller implements Serializable {
    private final PlayerController playerController;
    private final PropertyController propertyController;
    private final PropertyMapper mapper;
    private final MainCommandController commandController;
    private final TurnController turnController;
    private final Broadcaster broadcaster;
    private final EventDispatcher eventDispatcher;
    private final ViewController viewController;
    private final TradeController tradeController;
    private final RandomizationManager randomizationManager;
    private final Configuration configuration;
    private final Logger logger = LogManager.getLogger(getClass());
    private final Random random = new Random();
    private boolean gameStarted = false;
    private Board board;

    Controller() {
        this(null);
    }

    Controller(Configuration configuration) {
        this.configuration = configuration;
        List<PropertyModel> properties = Collections.emptyList();
        List<PlayerModel> players = new ArrayList<>();
        ObjectMapper jacksonMapper = new ObjectMapper();
        URL jsonURL = MainView.class.getClassLoader().getResource("properties.json");
        if (jsonURL != null) {
            try {
                properties = jacksonMapper.readValue(new File(jsonURL.toURI()), new TypeReference<>() {
                });
            } catch (IOException | URISyntaxException ignored) {
            }
        }
        mapper = new PropertyMapper(properties);

        viewController = new ViewController();

        playerController = new PlayerController(players, mapper);

        broadcaster = new Broadcaster();
        eventDispatcher = new MainEventDispatcher(broadcaster, viewController);

        randomizationManager = new RandomizationManager(properties, configuration);
        turnController = new TurnController(playerController, eventDispatcher, randomizationManager);

        PriceManagerDispatcher priceManagerDispatcher = new PriceManagerDispatcher(
                randomizationManager.getPropertyRandomizerManager(),
                mapper,
                mapper,
                eventDispatcher.diceRollEvent()
        );
        propertyController = new PropertyController(properties, priceManagerDispatcher, mapper, mapper);

        tradeController = new TradeController(playerController, propertyController);

        CommandBuilderDispatcher builderDispatcher = new MainCommandBuilderDispatcher(
                propertyController,
                playerController,
                tradeController,
                eventDispatcher
        );
        board = new Board(builderDispatcher, eventDispatcher, playerController, propertyController);
        commandController = new MainCommandController(builderDispatcher);
    }

    public synchronized PlayerModel setupPlayer(MainView view) {
        PlayerModel player = new PlayerModel(RandomStringUtils.randomAlphanumeric(6), "nome");
        viewController.setUp(player, view);
        synchronized (propertyController.getModels()) {
            playerController.addPlayer(player, 1000);
        }
        PlayerManager manager = playerController.getManager(player);
        mapper.register(manager);
        board.register(viewController.getPlayerPositionObserver(player));
        broadcaster.registerForOffers(viewController.getAuctionConsumer(player));
        broadcaster.registerForPlayers(viewController.getAllPlayersConsumer(player));
        broadcaster.registerForMessages(viewController.getMessageConsumer(player));
        manager.register(viewController.getPlayerObserver(player));
        manager.getPositionObservable().register(board);
        manager.register(turnController);
        manager.register(broadcaster.getPlayerObserver());
        LogManager.getLogger(getClass()).info("Adding new player {}#{}", player.getName(), player.getId());
        return player;
    }

    public void closePlayerSession(PlayerModel player) {
        synchronized (playerController.getModels()) {

            logger.info("Removing player {}#{}", player.getName(), player.getId());

            PlayerManager manager = playerController.getManager(player);

            mapper.deregister(manager);
            board.deregister(viewController.getPlayerPositionObserver(player));
            board.removePlayer(player);
            broadcaster.deregisterFromOffers(viewController.getAuctionConsumer(player));
            broadcaster.deregisterForPlayers(viewController.getAllPlayersConsumer(player));
            broadcaster.deregisterForMessages(viewController.getMessageConsumer(player));
            manager.deregister(viewController.getPlayerObserver(player));
            manager.getPositionObservable().deregister(board);
            manager.deregister(turnController);
            manager.deregister(broadcaster.getPlayerObserver());

            manager.setDiceRolled();
            manager.endTurn();
            mapper.removeAllPlayerProperties(player);

            playerController.removePlayer(player);
            manager.deregister(turnController);
            viewController.remove(player);


            if (playerController.getModels().isEmpty()) {
                stopGame();
            }
        }
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

    public List<ReadablePlayerModel> getPlayers() {
        List<ReadablePlayerModel> list = new LinkedList<>();
        for (PlayerModel model : playerController.getModels()) {
            list.add(playerController.getManager(model).getReadable());
        }
        return list;
    }

    public ReadablePlayerModel getReadablePlayer(PlayerModel playerModel) {
        return playerController.getManager(playerModel).getReadable();
    }

    public List<ReadablePropertyModel> getReadableProperties(PlayerModel playerModel) {
        List<ReadablePropertyModel> list = new ArrayList<>();
        for (PropertyModel property : mapper.getPlayerProperties(playerModel)) {
            ReadablePropertyModel readable = propertyController.getManager(property).getReadable();
            list.add(readable);
        }
        return list;
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
        int index = random.nextInt(models.size());
        PropertyModel property = models.get(index);
        LogManager.getLogger(getClass()).info("New property ({}) {} to player {}", random, property, player.getId());
        propertyController.getManager(property).setOwner(player);
    }

    public void stopGame() {
        logger.info("Stopping game");
        turnController.stop();
        gameStarted = false;
    }

    public void startAuction(PlayerModel player) {
        List<PropertyModel> models = propertyController.getModels();
        int index = random.nextInt(models.size());
        eventDispatcher.startOffer(new AuctionManager(player, models.get(index), tradeController));
    }

    public void startSell(PlayerModel player) {
        List<PropertyModel> models = propertyController.getModels();
        int index = random.nextInt(models.size());
        eventDispatcher.startOffer(new SellManager(player, models.get(index), tradeController));
    }
}
