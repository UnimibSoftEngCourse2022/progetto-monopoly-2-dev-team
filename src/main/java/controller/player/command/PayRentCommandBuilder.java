package controller.player.command;

import controller.command.CommandBuilder;
import controller.property.PropertyController;
import model.player.PlayerModel;
import model.property.PropertyModel;

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
