package it.monopoly.controller;

import it.monopoly.model.Configuration;
import it.monopoly.view.Observer;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RouteController implements Observer<Controller> {
    private final Logger logger = LogManager.getLogger(getClass());
    private final Map<String, Controller> controllerMap = new ConcurrentHashMap<>();

    public Controller getController(String id) {
        return controllerMap.getOrDefault(id, null);
    }

    public String createGame(Configuration configuration) {
        String id = RandomStringUtils.randomAlphanumeric(16).toLowerCase();
        Controller controller = new Controller(id, configuration);
        controller.register(this);
        controllerMap.put(id, controller);
        return id;
    }

    @Override
    public void notify(Controller controller) {
        if (!controller.isGameStarted() && controller.getPlayers().isEmpty()) {
            logger.info("Removing controller {}", controller);
            controllerMap.remove(controller.getId());
        }
    }
}
