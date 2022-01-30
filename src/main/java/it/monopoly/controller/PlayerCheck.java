package it.monopoly.controller;

import it.monopoly.controller.player.PlayerController;
import it.monopoly.controller.property.PropertyController;
import it.monopoly.manager.player.PlayerManager;
import it.monopoly.manager.property.PropertyManager;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.player.PlayerState;
import it.monopoly.model.property.PropertyModel;

public class PlayerCheck {
    private final PropertyController propertyController;
    private final PlayerController playerController;

    public PlayerCheck(PropertyController propertyController,
                       PlayerController playerController) {
        this.propertyController = propertyController;
        this.playerController = playerController;
    }

    public PlayerModel checkPlayers() {
        int freeCounter = 0;
        PlayerModel potentialWinner = null;
        for (PlayerModel model : playerController.getModels()) {
            PlayerManager manager = playerController.getManager(model);
            if (PlayerState.BANKRUPT.equals(manager.getState()) && !manager.getProperties().isEmpty()) {
                removeBuildings(manager);
                manager.removeAllProperties();
                playerController.removePlayer(model);
            } else {
                freeCounter++;
                potentialWinner = model;
            }
        }
        return freeCounter == 1 ? potentialWinner : null;
    }

    private synchronized void removeBuildings(PlayerManager manager) {
        for (PropertyModel property : manager.getProperties()) {
            PropertyManager propertyManager = propertyController.getManager(property);
            propertyManager.removeAllBuildings();
        }
    }
}
