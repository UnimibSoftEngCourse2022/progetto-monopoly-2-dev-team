package it.monopoly.controller.board;

import it.monopoly.controller.board.card.DrawableCardController;
import it.monopoly.controller.board.dispenser.*;
import it.monopoly.controller.board.space.Space;
import it.monopoly.controller.command.CommandBuilderDispatcher;
import it.monopoly.controller.event.EventDispatcher;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.controller.property.PropertyController;
import it.monopoly.manager.player.PlayerManager;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.view.Observable;
import it.monopoly.view.Observer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class Board implements Observer<PlayerModel>, Observable<PlayerPosition> {
    private final Logger logger = LogManager.getLogger(getClass());
    private final Map<PlayerModel, PlayerPosition> positionMap = new HashMap<>();
    private final List<Observer<PlayerPosition>> observers = new LinkedList<>();
    private final List<Space> spaces;
    private final PlayerController playerController;

    public Board(CommandBuilderDispatcher commandBuilderDispatcher,
                 EventDispatcher eventDispatcher,
                 DrawableCardController drawableCardController,
                 PlayerController playerController,
                 PropertyController propertyController) {
        this.playerController = playerController;
        this.spaces = new ArrayList<>();

        SpaceDispenser chainOfResponsibility = new TaxSpaceDispenser(commandBuilderDispatcher, 4, 38);
        chainOfResponsibility
                .setSuccessor(new ChanceSpaceDispenser(commandBuilderDispatcher, drawableCardController, playerController, eventDispatcher, 7, 22, 36))
                .setSuccessor(new CommunityChestSpaceDispenser(commandBuilderDispatcher, drawableCardController, playerController, eventDispatcher, 2, 17, 33))
                .setSuccessor(new GoToJailSpaceDispenser(commandBuilderDispatcher, 30))
                .setSuccessor(new CornerSpaceDispenser(commandBuilderDispatcher, 0, 10, 20))
                .setSuccessor(new PropertySpaceDispenser(commandBuilderDispatcher, propertyController));
        for (int i = 0; i < 40; i++) {
            spaces.add(chainOfResponsibility.processSpace(i));
        }
    }

    public List<Space> getSpaces() {
        return spaces;
    }

    public Space getSpace(int position) {
        if (position >= 0 && position < spaces.size()) {
            return spaces.get(position);
        }
        return null;
    }

    @Override
    public void notify(PlayerModel player) {
        PlayerManager manager = playerController.getManager(player);
        int position = manager.getPosition().getIntPosition();

        String stringPosition = manager.isInJail() ? "jail" : String.valueOf(position);

        PlayerPosition playerPosition = updateAndGetPosition(player, stringPosition);
        notifyObservers(playerPosition);

        Space space = getSpace(position);
        logger.info("Applying effect of space {} -> {}", position, space != null ? space.getClass() : null);
        if (space != null) {
            space.applyEffect(player);
        }
    }

    private PlayerPosition updateAndGetPosition(PlayerModel player, String stringPosition) {
        PlayerPosition playerPosition = new PlayerPosition(
                player.getName(),
                player.getId(),
                player.getColor(),
                stringPosition
        );
        if (stringPosition != null) {
            positionMap.put(player, playerPosition);
        }
        return playerPosition;
    }

    private void notifyObservers(PlayerPosition playerPosition) {
        for (Observer<PlayerPosition> observer : observers) {
            observer.notify(playerPosition);
        }
    }

    @Override
    public void register(Observer<PlayerPosition> observer) {
        if (observer != null) {
            observers.add(observer);
            for (PlayerPosition playerPosition : positionMap.values()) {
                observer.notify(playerPosition);
            }
        }
    }

    @Override
    public void deregister(Observer<PlayerPosition> observer) {
        if (observer != null) {
            observers.remove(observer);
        }
    }

    public void removePlayer(PlayerModel player) {
        positionMap.remove(player);
        PlayerPosition playerPosition = updateAndGetPosition(player, null);
        notifyObservers(playerPosition);
    }
}
