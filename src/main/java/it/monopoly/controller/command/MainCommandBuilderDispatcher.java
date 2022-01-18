package it.monopoly.controller.command;

import it.monopoly.controller.TradeController;
import it.monopoly.controller.event.EventDispatcher;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.controller.player.command.*;
import it.monopoly.controller.property.PropertyController;
import it.monopoly.controller.property.command.PropertyCommandBuilder;

public class MainCommandBuilderDispatcher implements CommandBuilderDispatcher {
    private final PropertyController propertyController;
    private final PlayerController playerController;
    private final TradeController tradeController;
    private final EventDispatcher eventDispatcher;

    public MainCommandBuilderDispatcher(PropertyController propertyController,
                                        PlayerController playerController,
                                        TradeController tradeController,
                                        EventDispatcher eventDispatcher) {
        this.propertyController = propertyController;
        this.playerController = playerController;
        this.tradeController = tradeController;
        this.eventDispatcher = eventDispatcher;
    }

    public <T> T createCommandBuilder(Class<T> className) {
        if (className == null) {
            return null;
        }
        if (PropertyCommandBuilder.class.equals(className)) {
            return className.cast(new PropertyCommandBuilder(propertyController, tradeController));
        } else if (PayCommandBuilder.class.equals(className)) {
            return className.cast(new PayCommandBuilder(tradeController));
        } else if (PayRentCommandBuilder.class.equals(className)) {
            return className.cast(new PayRentCommandBuilder(propertyController, new PayCommandBuilder(tradeController)));
        } else if (MoveCommandBuilder.class.equals(className)) {
            return className.cast(new MoveCommandBuilder(playerController));
        } else if (DiceRollCommandBuilder.class.equals(className)) {
            return className.cast(new DiceRollCommandBuilder(
                    playerController,
                    eventDispatcher,
                    new MoveCommandBuilder(playerController),
                    new PayCommandBuilder(tradeController)
            ));
        }
        return null;
    }
}
