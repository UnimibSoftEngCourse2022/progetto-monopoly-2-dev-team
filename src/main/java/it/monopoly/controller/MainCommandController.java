package it.monopoly.controller;

import it.monopoly.controller.command.Command;
import it.monopoly.controller.command.CommandBuilderDispatcher;
import it.monopoly.controller.player.command.DiceRollCommandBuilder;
import it.monopoly.controller.player.command.EndTurnCommandBuilder;
import it.monopoly.controller.property.command.PropertyCommandBuilder;
import it.monopoly.controller.property.command.SellPropertyCommandBuilder;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyModel;

import java.util.ArrayList;
import java.util.List;

public class MainCommandController {
    private final CommandBuilderDispatcher commandBuilderDispatcher;

    public MainCommandController(CommandBuilderDispatcher commandBuilderDispatcher) {
        this.commandBuilderDispatcher = commandBuilderDispatcher;
    }

    public List<Command> generateCommands(PropertyModel property) {
        List<Command> commands = new ArrayList<>();
        PropertyCommandBuilder propertyCommandBuilder =
                commandBuilderDispatcher.createCommandBuilder(PropertyCommandBuilder.class).setProperty(property);
        commands.add(propertyCommandBuilder.setType(PropertyCommandBuilder.Type.MORTGAGE).build());
        if (property.isImprovable()) {
            commands.add(propertyCommandBuilder.setType(PropertyCommandBuilder.Type.BUILD).build());
            commands.add(propertyCommandBuilder.setType(PropertyCommandBuilder.Type.SELL).build());
        }
        SellPropertyCommandBuilder sellPropertyCommandBuilder =
                commandBuilderDispatcher.createCommandBuilder(SellPropertyCommandBuilder.class).setProperty(property);
        commands.add(sellPropertyCommandBuilder.setProperty(property).build());
        return commands;
    }

    public List<Command> generateCommands(PlayerModel player) {
        ArrayList<Command> commands = new ArrayList<>();
        commands.add(commandBuilderDispatcher.createCommandBuilder(DiceRollCommandBuilder.class).setPlayer(player).build());
        commands.add(commandBuilderDispatcher.createCommandBuilder(EndTurnCommandBuilder.class).setPlayer(player).build());
        return commands;
    }
}
