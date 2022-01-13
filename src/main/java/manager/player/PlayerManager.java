package manager.player;

import model.player.PlayerModel;
import model.player.PlayerState;
import model.player.Position;

public class PlayerManager {
    private final PlayerModel player;
    private int funds;
    private PlayerState state;
    private Position position;

    public PlayerManager(PlayerModel player, int funds) {
        this.player = player;
        this.funds = funds;
        this.state = PlayerState.FREE;
        this.position = new Position();
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
        state = PlayerState.FREE;
    }

    public void getOutOfJailWithFine() {
        state = PlayerState.FINED;
    }

    public boolean canMove() {
        PlayerState playerState = state;
        return !PlayerState.BANKRUPT.equals(playerState) && !PlayerState.IN_JAIL.equals(playerState);
    }

    public boolean canTakeTurn() {
        PlayerState playerState = state;
        return !PlayerState.BANKRUPT.equals(playerState);
    }

    public boolean isInJail() {
        return PlayerState.IN_JAIL.equals(state);
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
