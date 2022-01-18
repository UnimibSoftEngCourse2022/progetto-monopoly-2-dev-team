package it.monopoly.controller.player.command;

import it.monopoly.controller.command.CommandBuilder;
import it.monopoly.controller.property.PropertyController;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyModel;

public class PayRentCommandBuilder implements CommandBuilder {
    private final PropertyController propertyController;
    private final PayCommandBuilder payCommandBuilder;
    private PlayerModel player;
    private PropertyModel property;

    public PayRentCommandBuilder(PropertyController propertyController, PayCommandBuilder payCommandBuilder) {
        this.propertyController = propertyController;
        this.payCommandBuilder = payCommandBuilder;
    }

    public PayRentCommandBuilder setPlayer(PlayerModel player) {
        this.player = player;
        return this;
    }

    public PayRentCommandBuilder setProperty(PropertyModel property) {
        this.property = property;
        return this;
    }

    public PayRentCommand build() {
        return new PayRentCommand(propertyController, player, property, payCommandBuilder);
    }
}
