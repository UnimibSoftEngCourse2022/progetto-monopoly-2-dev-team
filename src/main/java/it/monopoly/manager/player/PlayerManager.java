package it.monopoly.manager.player;

import it.monopoly.manager.Manager;
import it.monopoly.model.DrawableCardModel;
import it.monopoly.model.PropertyOwnerMapper;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.player.PlayerState;
import it.monopoly.model.player.Position;
import it.monopoly.model.player.ReadablePlayerModel;
import it.monopoly.model.property.PropertyModel;
import it.monopoly.util.Pair;
import it.monopoly.view.Observable;
import it.monopoly.view.Observer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PlayerManager extends Manager<PlayerModel> implements Observable<ReadablePlayerModel>, Observer<List<PropertyModel>> {
    private final Logger logger = LogManager.getLogger(getClass());
    private boolean isTurn = false;
    private boolean hasRolledDice = false;
    private final List<Observer<ReadablePlayerModel>> observers = new LinkedList<>();
    private int funds;
    private List<DrawableCardModel> drawableCardModels = new ArrayList<>();
    private PlayerState state;
    private final Position position;
    private final PropertyOwnerMapper ownerMapper;
    private static final int EARN_ON_GO = 200; //TODO Check configuration
    private int getOutOfJailTries = 0;

    public PlayerManager(PlayerModel player, int funds, PropertyOwnerMapper ownerMapper) {
        super(player);
        this.funds = funds;
        this.state = PlayerState.FREE;
        this.position = new Position();
        this.ownerMapper = ownerMapper;
    }

    public boolean startTurn() {
        isTurn = canTakeTurn();
        if (isTurn) {
            logger.info("Player {} turn", model.getId());
            hasRolledDice = false;
            notifyReadable();
        }
        return isTurn;
    }

    public void endTurn() {
        if (canEndTurn()) {
            if (isTurn) {
                logger.info("Player {} ended turn", model.getId());
            }
            isTurn = false;
            notifyReadable();
        }
    }

    public boolean isTakingTurn() {
        return isTurn;
    }

    public void setDiceRolled() {
        hasRolledDice = true;
        notifyReadable();
    }

    public ReadablePlayerModel getReadable() {
        return new ReadablePlayerModel(model.getName(), funds, state, position, isTurn, getProperties());
    }

    public void earn(int money) {
        funds += money;
        logger.info("Player {} earned {}: new funds {}", model.getId(), money, funds);
        checkBankrupt();
        notifyReadable();
    }

    public void spend(int money) {
        funds -= money;
        logger.info("Player {} spent {}: new funds {}", model.getId(), money, funds);
        checkBankrupt();
        notifyReadable();
    }

    public Position move(int movement, boolean direct) {
        return moveTo((position.getIntPosition() + movement) % 40, direct);
    }

    public Position moveTo(int space, boolean direct) {
        if (canMove()) {
            position.setPosition(space, direct);
            logger.info("Player {} moved to space {}", model.getId(), space);
        }
        Pair<Integer, Integer> movement = position.getLastMovement().getMovement();
        if (!direct && movement.getFirst() > movement.getSecond()) {
            earn(EARN_ON_GO);
        }
        notifyReadable();
        return position;
    }

    public void goToJail() {
        state = PlayerState.IN_JAIL;
    }

    public boolean tryToGetOutOfJail() {
        if (PlayerState.IN_JAIL.equals(state)) {
            getOutOfJailTries++;
            logger.info("Player {} tried to get out of jail", model.getId());
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

    public List<DrawableCardModel> getDrawableCardModels() {
        return drawableCardModels;
    }

    public void getOutOfJail() {
        if (PlayerState.IN_JAIL.equals(state) || PlayerState.FINED.equals(state)) {
            getOutOfJailTries = 0;
            setState(PlayerState.FREE);
            logger.info("Player {} got out of jail", model.getId());
        }
    }

    public void getOutOfJailWithFine() {
        if (PlayerState.IN_JAIL.equals(state)) {
            setState(PlayerState.FINED);
            logger.info("Player {} got out of jail with fine", model.getId());
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

    public boolean canEndTurn() {
        return hasRolledDice();
    }

    public boolean hasRolledDice() {
        return hasRolledDice;
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

    public void setState(PlayerState state) {
        boolean changed = this.state != state;
        this.state = state;
        if (changed) {
            logger.info("Player {} changed its state from {} to {}", model.getId(), this.state, state);
            notifyReadable();
        }
    }

    public Position getPosition() {
        return position;
    }

    //PRIVATE METHODS

    private void checkBankrupt() {
        if (funds <= 0) {
            setState(PlayerState.BANKRUPT);
        } else if (PlayerState.BANKRUPT.equals(state)) {
            setState(PlayerState.FREE);
        }
    }

    public void notifyReadable() {
        if (!observers.isEmpty()) {
            ReadablePlayerModel readable = getReadable();
            for (Observer<ReadablePlayerModel> observer : observers) {
                observer.notify(readable);
            }
        }
    }

    @Override
    public void register(Observer<ReadablePlayerModel> observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    @Override
    public void deregister(Observer<ReadablePlayerModel> observer) {
        if (observer != null) {
            observers.remove(observer);
        }
    }

    @Override
    public void notify(List<PropertyModel> obj) {
        notifyReadable();
    }
}
