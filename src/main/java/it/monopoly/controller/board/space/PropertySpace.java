package it.monopoly.controller.board.space;

import it.monopoly.controller.command.CommandBuilderDispatcher;
import it.monopoly.controller.player.command.PayRentCommandBuilder;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyModel;

public class PropertySpace extends AbstractSpace {

    private final PropertyModel property;

    public PropertySpace(CommandBuilderDispatcher commandBuilderDispatcher, PropertyModel property) {
        super(commandBuilderDispatcher);
        this.property = property;
    }

    public PropertyModel getProperty() {
        return property;
    }

    @Override
    public void applyEffect(PlayerModel player) {
        commandBuilderDispatcher.createCommandBuilder(PayRentCommandBuilder.class)
                .setProperty(property)
                .setPlayer(player)
                .build()
                .execute();
    }
}
