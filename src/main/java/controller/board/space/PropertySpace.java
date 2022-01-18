package controller.board.space;

import controller.command.CommandBuilderDispatcher;
import model.player.PlayerModel;
import model.property.PropertyModel;

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

    }
}
