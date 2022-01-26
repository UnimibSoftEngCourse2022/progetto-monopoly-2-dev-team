package it.monopoly.controller.player.command;

import it.monopoly.controller.command.CommandBuilder;
import it.monopoly.controller.event.EventDispatcher;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.controller.property.PropertyController;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PayRentCommandBuilder implements CommandBuilder {
    private final Logger logger = LogManager.getLogger(getClass());
    private final PropertyController propertyController;
    private final PlayerController playerController;
    private final PayCommandBuilder payCommandBuilder;
    private final EventDispatcher eventDispatcher;
    private PlayerModel player;
    private PropertyModel property;

    public PayRentCommandBuilder(PropertyController propertyController,
                                 PlayerController playerController,
                                 PayCommandBuilder payCommandBuilder,
                                 EventDispatcher eventDispatcher) {
        this.propertyController = propertyController;
        this.playerController = playerController;
        this.payCommandBuilder = payCommandBuilder;
        this.eventDispatcher = eventDispatcher;
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
        logger.info("Building pay rent command");
        return new PayRentCommand(propertyController,
                playerController,
                player,
                property,
                payCommandBuilder,
                eventDispatcher);
    }
}
