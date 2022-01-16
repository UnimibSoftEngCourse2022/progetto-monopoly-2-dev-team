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

    private final CommandBuilderDispatcher commandBuilderDispatcher;

    public PlayerController(List<PlayerModel> players, PropertyOwnerMapper ownerMapper, CommandBuilderDispatcher commandBuilderDispatcher) {
        this.commandBuilderDispatcher = commandBuilderDispatcher;
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
    public List<Command> getCommands(PlayerModel player) {
        ArrayList<Command> commands = new ArrayList<>();
        commands.add(commandBuilderDispatcher.createCommandBuilder(DiceRollCommandBuilder.class).setPlayer(player).build());
        return commands;
    }
}
