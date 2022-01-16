package controller.command;

import controller.TradeController;
import controller.player.PlayerController;
import controller.player.command.MoveCommandBuilder;
import controller.player.command.PayCommandBuilder;
import controller.property.PropertyController;
import controller.property.command.*;

public class MainCommandBuilderDispatcher implements CommandBuilderDispatcher {
    private final PropertyController propertyController;
    private final PlayerController playerController;
    private final TradeController tradeController;

    public MainCommandBuilderDispatcher(PropertyController propertyController, PlayerController playerController, TradeController tradeController) {
        this.propertyController = propertyController;
        this.playerController = playerController;
        this.tradeController = tradeController;
    }

    public <T> T createCommandBuilder(Class<T> className) {
        if(className == null) {
            return null;
        }
        if(PropertyCommandBuilder.class.equals(className)) {
            return className.cast(new PropertyCommandBuilder(propertyController, tradeController));
        } else if(PayCommandBuilder.class.equals(className)) {
            return className.cast(new PayCommandBuilder(tradeController));
        } else if(MoveCommandBuilder.class.equals(className)) {
            return className.cast(new MoveCommandBuilder(playerController));
        }
        return null;
    }
}
