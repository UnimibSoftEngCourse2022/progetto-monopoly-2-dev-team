package it.monopoly.controller.player;

import it.monopoly.controller.ManagerController;
import it.monopoly.manager.player.PlayerManager;
import it.monopoly.model.PropertyOwnerMapper;
import it.monopoly.model.player.PlayerModel;

import java.util.List;

public class PlayerController extends ManagerController<PlayerModel, PlayerManager> {

    private final PropertyOwnerMapper ownerMapper;

    public PlayerController(PropertyOwnerMapper ownerMapper) {
        this(null, ownerMapper);
    }

    public PlayerController(List<PlayerModel> players, PropertyOwnerMapper ownerMapper) {
        this.ownerMapper = ownerMapper;
        if (players != null) {
            for (PlayerModel player : players) {
                addPlayer(player, 1000);
            }
        }
    }

    public boolean addPlayer(PlayerModel playerModel, int funds) {
        models.add(playerModel);
        modelToManagerMap.put(playerModel, new PlayerManager(playerModel, funds, ownerMapper));
        return true;
    }

    public void removePlayer(PlayerModel player) {
        models.remove(player);
        modelToManagerMap.remove(player);
    }
}
