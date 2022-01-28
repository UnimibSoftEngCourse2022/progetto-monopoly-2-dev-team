package it.monopoly.controller;

import it.monopoly.model.Configuration;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RouteController {
    private final Map<String, Controller> controllerMap = new ConcurrentHashMap<>();

    public Controller getController(String id) {
        return controllerMap.getOrDefault(id, null);
    }

    public String createGame(Configuration configuration) {
        String id = RandomStringUtils.randomAlphanumeric(16).toLowerCase();
        Controller controller = new Controller(configuration);
        controllerMap.put(id, controller);
        return id;
    }
}
