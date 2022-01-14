package manager.player;

import model.PropertyOwnerMapper;
import model.player.PlayerModel;
import model.player.PlayerState;
import model.player.Position;
import model.property.PropertyModel;

import java.util.List;

public class PlayerManager {
    private final PlayerModel player;
    private int funds;
    private PlayerState state;
    private Position position;
    private PropertyOwnerMapper ownerMapper;
    private final int EARN_ON_GO = 200; //TODO Check configuration

    public PlayerManager(PlayerModel player, int funds, PropertyOwnerMapper ownerMapper) {
        this.player = player;
        this.funds = funds;
        this.state = PlayerState.FREE;
        this.position = new Position();
        this.ownerMapper = ownerMapper;
    }

    public void earn(int money) {
        funds += money;
    }

    public void spend(int money) {
        funds -= money;
        checkBankrupt();
    }

    public Position move(int diceRoll, boolean direct) {
        if (canMove()) {
            position.setPosition(position.getIntPosition() + diceRoll, direct);
        }
        return position;
    }

    public void goToJail() {
        state = PlayerState.IN_JAIL;
    }

    public void getOutOfJail() {
        if (PlayerState.IN_JAIL.equals(state) || PlayerState.FINED.equals(state)) {
            state = PlayerState.FREE;
        }
    }

    public void getOutOfJailWithFine() {
        if (PlayerState.IN_JAIL.equals(state)) {
            state = PlayerState.FINED;
        }
    }

    public List<PropertyModel> getProperties() {
        return ownerMapper.getPlayerProperties(player);
    }

    public boolean canMove() {
        return !PlayerState.BANKRUPT.equals(state) && !PlayerState.IN_JAIL.equals(state);
    }

    public boolean canTakeTurn() {
        PlayerState playerState = state;
        return !PlayerState.BANKRUPT.equals(playerState);
    }

    public boolean isInJail() {
        return PlayerState.IN_JAIL.equals(state);
    }

    public int getFunds() {
        return funds;
    }

    public PlayerState getState() {
        return state;
    }

    //PRIVATE METHODS

    private void checkBankrupt() {
        if (funds <= 0) {
            state = PlayerState.BANKRUPT;
        } else if (PlayerState.BANKRUPT.equals(state)) {
            state = PlayerState.FREE;
        }
    }
}
