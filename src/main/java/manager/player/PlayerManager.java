package manager.player;

import manager.Manager;
import manager.loyaltyprogram.LoyaltyProgram;
import model.Configuration;
import model.DrawableCardModel;
import model.PropertyOwnerMapper;
import model.player.PlayerModel;
import model.player.PlayerState;
import model.player.Position;
import model.property.PropertyModel;
import util.Pair;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager extends Manager<PlayerModel> {

    private int funds;
    private PlayerState state;
    private Position position;
    private PropertyOwnerMapper ownerMapper;
    private List<DrawableCardModel> drawableCardModels = new ArrayList<>();
    private LoyaltyProgram loyaltyProgram = null;

    private static final int EARN_ON_GO = 200; //TODO Check configuration
    private int getOutOfJailTries = 0;


    public PlayerManager(PlayerModel player, int funds, PropertyOwnerMapper ownerMapper) {
        super(player);
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

    public Position move(int movement, boolean direct) {
        return moveTo((position.getIntPosition() + movement) % 40, direct);
    }

    public Position moveTo(int space, boolean direct) {
        if (canMove()) {
            position.setPosition(space, direct);
        }
        Pair<Integer, Integer> movement = position.getLastMovement().getMovement();
        if (!direct && movement.getFirst() > movement.getSecond()) {
            earn(EARN_ON_GO);
        }
        return position;
    }

    public void goToJail() {
        state = PlayerState.IN_JAIL;
    }

    public boolean tryToGetOutOfJail() {
        if (PlayerState.IN_JAIL.equals(state)) {
            getOutOfJailTries++;
            boolean succeeded = getOutOfJailTries == 3;
            if (succeeded) {
                getOutOfJail();
            }
            return succeeded;
        }
        return false;
    }

    public void keepCard(DrawableCardModel cardModel) {
        drawableCardModels.add(cardModel);
    }

    public void getOutOfJail() {
        if (PlayerState.IN_JAIL.equals(state) || PlayerState.FINED.equals(state)) {
            getOutOfJailTries = 0;
            state = PlayerState.FREE;
        }
    }

    public void getOutOfJailWithFine() {
        if (PlayerState.IN_JAIL.equals(state)) {
            state = PlayerState.FINED;
        }
    }

    public List<PropertyModel> getProperties() {
        return ownerMapper.getPlayerProperties(model);
    }

    public boolean canMove() {
        return !PlayerState.BANKRUPT.equals(state) && !PlayerState.IN_JAIL.equals(state);
    }

    public boolean canTakeTurn() {
        PlayerState playerState = state;
        return !PlayerState.BANKRUPT.equals(playerState);
    }

    public boolean joinLoyaltyProgram(LoyaltyProgram loyaltyProgram) {
        if (this.loyaltyProgram == null) {
            this.loyaltyProgram = loyaltyProgram;
            return true;
        }
        return false;
    }

    public LoyaltyProgram quitLoyaltyProgram() {
        LoyaltyProgram temp = loyaltyProgram;
        this.loyaltyProgram = null;
        return temp;
    }

    public boolean isInJail() {
        return PlayerState.IN_JAIL.equals(state);
    }

    public List<DrawableCardModel> getDrawableCardModels() {
        return drawableCardModels;
    }

    public int getFunds() {
        return funds;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public Position getPosition() {
        return position;
    }

    public LoyaltyProgram getLoyaltyProgram() {
        return loyaltyProgram;
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
