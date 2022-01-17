package controller.player;

import controller.ManagerController;
import controller.command.Command;
import controller.command.CommandBuilderDispatcher;
import controller.player.command.DiceRollCommandBuilder;
import manager.player.PlayerManager;
import model.PropertyOwnerMapper;
import model.player.PlayerModel;

import java.util.ArrayList;
import java.util.List;

public class PlayerController extends ManagerController<PlayerModel, PlayerManager> {

    public PlayerController(List<PlayerModel> players, PropertyOwnerMapper ownerMapper) {
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
}
