package it.monopoly.controller;

import it.monopoly.controller.command.Command;
import it.monopoly.controller.command.CommandBuilderDispatcher;
import it.monopoly.controller.player.command.DiceRollCommandBuilder;
import it.monopoly.controller.player.command.EndTurnCommandBuilder;
import it.monopoly.controller.player.command.GetOutOfJailCommandBuilder;
import it.monopoly.controller.player.command.LoyaltyProgramCommandBuilder;
import it.monopoly.controller.property.command.PropertyCommandBuilder;
import it.monopoly.controller.property.command.SellPropertyCommandBuilder;
import it.monopoly.model.Configuration;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyModel;

import java.util.LinkedList;
import java.util.List;

public class MainCommandController {
    private final CommandBuilderDispatcher commandBuilderDispatcher;
    private final Configuration configuration;

    public MainCommandController(CommandBuilderDispatcher commandBuilderDispatcher, Configuration configuration) {
        this.commandBuilderDispatcher = commandBuilderDispatcher;
        this.configuration = configuration;
    }

    public List<Command> generateCommands(PropertyModel property) {
        List<Command> commands = new LinkedList<>();
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
        List<Command> commands = new LinkedList<>();
        commands.add(commandBuilderDispatcher.createCommandBuilder(DiceRollCommandBuilder.class).setPlayer(player).build());
        commands.add(commandBuilderDispatcher.createCommandBuilder(GetOutOfJailCommandBuilder.class).setPlayer(player).build());
        commands.add(commandBuilderDispatcher.createCommandBuilder(EndTurnCommandBuilder.class).setPlayer(player).build());
        return commands;
    }

    public List<Command> generateCommand(PlayerModel player, PlayerModel selectedPlayer) {
        List<Command> commands = new LinkedList<>();
        if (configuration.getLoyaltyType() != null) {
            commands.add(commandBuilderDispatcher.createCommandBuilder(LoyaltyProgramCommandBuilder.class)
                    .setType(configuration.getLoyaltyType())
                    .setDebtor(player)
                    .setCreditor(selectedPlayer)
                    .build());
        }
        return commands;
    }
}
