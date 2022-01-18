package it.monopoly;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.monopoly.controller.MainCommandController;
import it.monopoly.controller.TradeController;
import it.monopoly.controller.command.MainCommandBuilderDispatcher;
import it.monopoly.controller.event.MainEventDispatcher;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.controller.property.PropertyController;
import it.monopoly.model.PropertyMapper;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyModel;
import it.monopoly.view.MainView;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

@Component
public class Controller {

    private final PlayerController playerController;
    private final PropertyController propertyController;
    private final PropertyMapper mapper;
    private final MainCommandController commandController;

    Controller() {
        List<PropertyModel> properties = Collections.emptyList();
        List<PlayerModel> players = Collections.emptyList();
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
        MainEventDispatcher eventDispatcher = new MainEventDispatcher();
        MainCommandBuilderDispatcher builderDispatcher = new MainCommandBuilderDispatcher(propertyController, playerController, tradeController, eventDispatcher);
        commandController = new MainCommandController(builderDispatcher);
    }

    public List<PropertyModel> getProperties() {
        return propertyController.getModels();
    }

    public List<PlayerModel> getPlayers() {
        return playerController.getModels();
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
}
