package it.monopoly.manager.player;

import it.monopoly.manager.Manager;
import it.monopoly.manager.loyaltyprogram.LoyaltyProgram;
import it.monopoly.model.DrawableCardModel;
import it.monopoly.model.PropertyOwnerMapper;
import it.monopoly.model.player.*;
import it.monopoly.model.property.PropertyModel;
import it.monopoly.util.Pair;
import it.monopoly.view.Observable;
import it.monopoly.view.Observer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PlayerManager extends Manager<PlayerModel> implements Observable<ReadablePlayerModel>, Observer<PlayerModel> {
    private static final int EARN_ON_GO = 200; //TODO Check configuration
    public final Object lock = new Object();
    private final Logger logger = LogManager.getLogger(getClass());
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final List<Observer<ReadablePlayerModel>> observers = new LinkedList<>();
    private final List<Observer<PlayerModel>> positionObservers = new LinkedList<>();
    private final Position position;
    private final PropertyOwnerMapper ownerMapper;
    private boolean isTurn = false;
    private boolean hasRolledDice = false;
    private int funds;
    private final List<DrawableCardModel> drawableCardModels = new ArrayList<>();
    private PlayerState state;
    private int getOutOfJailTries = 0;
    private int joinLoyaltyInTurns = 0;
    private int freeInTurns = 0;
    private LoyaltyProgram loyaltyProgram = null;
    private Observable<PlayerModel> positionObservable;

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
            logger.info("Player {}#{} turn", model.getName(), model.getId());
            hasRolledDice = false;
            notifyReadable();
        }
        return isTurn;
    }

    public void endTurn() {
        if (canEndTurn()) {
            if (joinLoyaltyInTurns > 0) joinLoyaltyInTurns--;
            if (isTurn) {
                logger.info("Player {}#{} ended turn", model.getName(), model.getId());
            }
            isTurn = false;
            checkFineState();
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

    public synchronized ReadablePlayerModel getReadable() {
        return new ReadablePlayerModel(
                model,
                funds,
                state,
                position,
                isTurn,
                PlayerState.BANKRUPT.equals(state) ? Collections.emptyList() : getProperties()
        );
    }

    public void earn(int money) {
        funds += money;
        logger.info("Player {}#{} earned {}: new funds {}", model.getName(), model.getId(), money, funds);
        checkBankrupt();
        notifyReadable();
    }

    public void spend(int money) {
        funds -= money;
        logger.info("Player {}#{} spent {}: new funds {}", model.getName(), model.getId(), money, funds);
        checkBankrupt();
        notifyReadable();
    }

    public Position move(int movement, boolean direct) {
        return moveTo((position.getIntPosition() + movement) % 40, direct);
    }

    public Position moveTo(int space, boolean direct) {
        synchronized (lock) {
            if (canMove()) {
                position.setPosition(space, direct);
                notifyPosition();
                logger.info("Player {}#{} moved to space {}", model.getName(), model.getId(), space);
            }
            PlayerMovement lastMovement = position.getLastMovement();
            Pair<Integer, Integer> movement = lastMovement.getMovement();
            if (!lastMovement.isDirect() && movement.getFirst() > movement.getSecond()) {
                earn(EARN_ON_GO);
            }
        }
        return position;
    }

    public void goToJail() {
        setState(PlayerState.IN_JAIL);
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

    public boolean joinLoyaltyProgram(LoyaltyProgram loyaltyProgram) {
        if (this.loyaltyProgram == null && joinLoyaltyInTurns == 0) {
            this.loyaltyProgram = loyaltyProgram;
            notifyReadable();
            return true;
        }
        return false;
    }

    public LoyaltyProgram quitLoyaltyProgram() {
        LoyaltyProgram temp = loyaltyProgram;
        this.loyaltyProgram = null;
        joinLoyaltyInTurns = 4;
        return temp;
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
            freeInTurns = 3;
            logger.info("Player {} got out of jail with fine", model.getId());
        }
    }

    public synchronized List<PropertyModel> getProperties() {
        return ownerMapper.getPlayerProperties(model);
    }

    public synchronized List<PropertyModel> removeAllProperties() {
        return ownerMapper.removeAllPlayerProperties(model);
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

    public LoyaltyProgram getLoyaltyProgram() {
        return loyaltyProgram;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        PlayerState previous = this.state;
        boolean changed = previous != state;
        this.state = state;
        if (changed) {
            logger.info("Player {} changed its state from {} to {}", model.getId(), previous, state);
            if (PlayerState.BANKRUPT.equals(state)) {
                endTurn();
            }
            notifyReadable();
        }
    }

    public Position getPosition() {
        return position;
    }


    //PRIVATE METHODS
    private void checkFineState() {
        if (freeInTurns > 0) {
            freeInTurns--;
            if (freeInTurns == 0 && PlayerState.FINED.equals(state)) {
                setState(PlayerState.FREE);
            }
        }
    }

    private void checkBankrupt() {
        if (funds <= 0) {
            setState(PlayerState.BANKRUPT);
        }
    }

    public void notifyReadable() {
        synchronized (lock) {
            if (!observers.isEmpty()) {
                ReadablePlayerModel readable = getReadable();
                for (Observer<ReadablePlayerModel> observer : observers) {
                    executor.execute(() -> observer.notify(readable));
                }
            }
        }
    }

    @Override
    public void register(Observer<ReadablePlayerModel> observer) {
        if (observer != null) {
            observers.add(observer);
            observer.notify(getReadable());
        }
    }

    @Override
    public void deregister(Observer<ReadablePlayerModel> observer) {
        if (observer != null) {
            observers.remove(observer);
        }
    }

    @Override
    public void notify(PlayerModel playerModel) {
        if (model.equals(playerModel)) {
            notifyReadable();
        }
    }

    public Observable<PlayerModel> getPositionObservable() {
        if (positionObservable == null) {
            positionObservable = new Observable<>() {
                @Override
                public void register(Observer<PlayerModel> observer) {
                    PlayerManager.this.registerPositionObserver(observer);
                }

                @Override
                public void deregister(Observer<PlayerModel> observer) {
                    PlayerManager.this.deregisterPositionObserver(observer);
                }
            };
        }
        return positionObservable;
    }

    private void registerPositionObserver(Observer<PlayerModel> observer) {
        if (observer != null) {
            positionObservers.add(observer);
            observer.notify(model);
        }
    }

    private void deregisterPositionObserver(Observer<PlayerModel> observer) {
        if (observer != null) {
            positionObservers.remove(observer);
        }
    }

    private void notifyPosition() {
        for (Observer<PlayerModel> observer : positionObservers) {
            executor.execute(() -> observer.notify(model));
        }
    }

    public boolean canJoinLoyalty() {
        return loyaltyProgram == null && canJoinLoyaltyInTurns() == 0;
    }

    public int canJoinLoyaltyInTurns() {
        return joinLoyaltyInTurns;
    }
}
