package it.monopoly.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.monopoly.Broadcaster;
import it.monopoly.controller.board.Board;
import it.monopoly.controller.board.card.DrawableCardController;
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
import it.monopoly.view.Observable;
import it.monopoly.view.Observer;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class Controller implements Serializable, Observable<Controller> {
    private final List<Observer<Controller>> observers = new LinkedList<>();
    private final String id;
    private final Configuration configuration;
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
    private final Logger logger = LogManager.getLogger(getClass());
    private final Random random = new Random();
    private boolean gameStarted = false;
    private final Board board;

    Controller(String id) {
        this(id, new Configuration());
    }

    Controller(String id, Configuration configuration) {
        this.id = id;
        this.configuration = configuration;
        List<PropertyModel> properties = Collections.emptyList();
        ObjectMapper jacksonMapper = new ObjectMapper();
        URL jsonURL = Controller.class.getClassLoader().getResource("properties.json");
        if (jsonURL != null) {
            try {
                properties = jacksonMapper.readValue(new File(jsonURL.toURI()), new TypeReference<>() {
                });
            } catch (IOException | URISyntaxException ignored) {
                logger.error("Error while parsing properties json file");
            }
        }
        mapper = new PropertyMapper(properties);

        viewController = new ViewController();

        playerController = new PlayerController(mapper);

        broadcaster = new Broadcaster();

        eventDispatcher = new MainEventDispatcher(broadcaster, viewController);

        randomizationManager = new RandomizationManager(properties, configuration);

        PriceManagerDispatcher priceManagerDispatcher = new PriceManagerDispatcher(
                randomizationManager,
                mapper,
                mapper,
                eventDispatcher.diceRollEvent()
        );
        propertyController = new PropertyController(properties, priceManagerDispatcher, mapper, mapper);

        turnController = new TurnController(propertyController, playerController, eventDispatcher, randomizationManager);

        tradeController = new TradeController(playerController, propertyController);

        DrawableCardController drawableCardController = new DrawableCardController();
        CommandBuilderDispatcher builderDispatcher = new MainCommandBuilderDispatcher(
                propertyController,
                playerController,
                drawableCardController,
                tradeController,
                eventDispatcher
        );
        board = new Board(
                builderDispatcher,
                eventDispatcher,
                drawableCardController,
                randomizationManager,
                playerController,
                propertyController
        );
        commandController = new MainCommandController(builderDispatcher, configuration);
    }

    public synchronized PlayerModel setupPlayer(String playerName, MainView view) {
        if (maxPlayerReached()) {
            return null;
        }
        PlayerModel player = new PlayerModel(RandomStringUtils.randomAlphanumeric(6), playerName);
        viewController.setUp(player, view);
        synchronized (propertyController.getModels()) {
            playerController.addPlayer(player, configuration.getPlayerFund());
        }
        PlayerManager manager = playerController.getManager(player);
        mapper.getOwnerObservable().register(manager);
        board.register(viewController.getPlayerPositionObserver(player));
        broadcaster.registerForOffers(viewController.getOfferConsumer(player));
        broadcaster.registerForPlayers(viewController.getAllPlayersConsumer(player));
        broadcaster.registerForMessages(viewController.getMessageConsumer(player));
        broadcaster.registerForWinner(viewController.getWinnerConsumer(player));
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

            mapper.getOwnerObservable().deregister(manager);
            mapper.removeAllPlayerProperties(player);
            board.deregister(viewController.getPlayerPositionObserver(player));
            board.removePlayer(player);
            broadcaster.deregisterFromOffers(viewController.getOfferConsumer(player));
            broadcaster.deregisterForPlayers(viewController.getAllPlayersConsumer(player));
            broadcaster.deregisterForMessages(viewController.getMessageConsumer(player));
            broadcaster.deregisterForWinner(viewController.getWinnerConsumer(player));
            broadcaster.notifyAllPlayers(manager.getReadable());
            manager.setDiceRolled();
            manager.endTurn();
            manager.deregister(viewController.getPlayerObserver(player));
            manager.getPositionObservable().deregister(board);
            manager.deregister(turnController);
            manager.deregister(broadcaster.getPlayerObserver());

            playerController.removePlayer(player);
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

    public String getId() {
        return id;
    }

    public boolean maxPlayerReached() {
        return getPlayers().size() >= configuration.getPlayerNumber();
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

    public List<PlayerModel> getPlayerModels() {
        return playerController.getModels();
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
        for (Observer<Controller> observer : observers) {
            observer.notify(this);
        }
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

    @Override
    public void register(Observer<Controller> observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    @Override
    public void deregister(Observer<Controller> observer) {
        if (observer != null) {
            observers.remove(observer);
        }
    }
}
