package controller.player;

import controller.ManagerController;
import controller.command.Command;
import controller.command.ModelCommand;
import manager.player.PlayerManager;
import model.PropertyOwnerMapper;
import model.player.PlayerModel;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerController extends ManagerController<PlayerModel, PlayerManager> {

    public PlayerController(List<PlayerModel> players, PropertyOwnerMapper ownerMapper) {
        this.models = players;
        for (PlayerModel player : players) {
            addPlayer(player, 1000, ownerMapper); //TODO Connect to game configuration
        }
    }

    public boolean addPlayer(PlayerModel playerModel, int funds, PropertyOwnerMapper ownerMapper) {
        models.add(playerModel);
        modelToManagerMap.put(playerModel, new PlayerManager(playerModel, funds, ownerMapper));
        return true;
    }

    public void removePlayer(PlayerModel player) {
        models.remove(player);
        modelToManagerMap.remove(player);
    }

    @Override
    public List<Command> getCommands(PlayerModel model) {
        return Collections.emptyList();
    }
}
