package it.monopoly.model.player;

import it.monopoly.model.property.PropertyModel;

import java.io.Serializable;
import java.util.List;

public class ReadablePlayerModel implements Serializable {
    private final String name;
    private final int funds;
    private final PlayerState state;
    private final Position position;
    private final boolean isTurn;
    private final List<PropertyModel> properties;

    public ReadablePlayerModel(String name, int funds, PlayerState state, Position position, boolean isTurn, List<PropertyModel> properties) {
        this.name = name;
        this.funds = funds;
        this.state = state;
        this.isTurn = isTurn;
        this.properties = properties;
        this.position = new Position();
        this.position.setPosition(position.getLastMovement().getMovement().getFirst());
        this.position.setPosition(position.getIntPosition(), position.getLastMovement().isDirect());
    }

    public List<PropertyModel> getProperties() {
        return properties;
    }

    public String getName() {
        return name;
    }

    public int getFunds() {
        return funds;
    }

    public PlayerState getState() {
        return state;
    }

    public String getStateName() {
        if (PlayerState.FREE.equals(state)) {
            return "Free";
        } else if (PlayerState.IN_JAIL.equals(state)) {
            return "In jail";
        } else if (PlayerState.FINED.equals(state)) {
            return "Fined";
        } else {
            return "Bankrupted";
        }
    }

    public Position getPosition() {
        return position;
    }

    public boolean isTurn() {
        return isTurn;
    }
}
