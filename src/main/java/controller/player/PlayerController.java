package controller.player;

import manager.player.PlayerManager;
import model.PropertyOwnerMapper;
import model.player.PlayerModel;

import java.util.List;
import java.util.Map;

public class PlayerController {
    private List<PlayerModel> players;
    private Map<PlayerModel, PlayerManager> playerManagerMap;

    public PlayerController(List<PlayerModel> players, PropertyOwnerMapper ownerMapper) {
        this.players = players;
        for (PlayerModel player : players) {
            addPlayer(player, 1000, ownerMapper); //TODO Connect to game configuration
        }
    }

    public boolean addPlayer(PlayerModel playerModel, int funds, PropertyOwnerMapper ownerMapper) {
        players.add(playerModel);
        playerManagerMap.put(playerModel, new PlayerManager(playerModel, funds, ownerMapper));
        return true;
    }

    public void removePlayer(PlayerModel player) {
        players.remove(player);
        playerManagerMap.remove(player);
    }

    public PlayerManager getManager(PlayerModel player) {
        return playerManagerMap.get(player);
    }
}
