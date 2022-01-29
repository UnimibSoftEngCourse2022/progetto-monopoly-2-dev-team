package it.monopoly.model;

import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyCategory;
import it.monopoly.model.property.PropertyModel;
import it.monopoly.view.Observable;
import it.monopoly.view.Observer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PropertyMapper implements PropertyOwnerMapper, PropertyCategoryMapper {
    private final List<Observer<PlayerModel>> ownerObservers = new LinkedList<>();

    private final Map<PlayerModel, List<PropertyModel>> playerProperties = new ConcurrentHashMap<>();
    private final Map<PropertyModel, PlayerModel> propertyOwner = new ConcurrentHashMap<>();
    private final Map<PropertyCategory, List<PropertyModel>> propertyCategory = new ConcurrentHashMap<>();

    private final Observable<PlayerModel> ownerObservable = new Observable<>() {
        @Override
        public void register(Observer<PlayerModel> observer) {
            PropertyMapper.this.registerOwnerObserver(observer);
        }

        @Override
        public void deregister(Observer<PlayerModel> observer) {
            PropertyMapper.this.deregisterOwnerObserver(observer);
        }
    };

    public PropertyMapper(List<PropertyModel> properties) {
        for (PropertyModel property : properties) {
            propertyCategory.computeIfAbsent(property.getCategory(), k -> new ArrayList<>()).add(property);
        }
    }

    public List<PropertyModel> getPlayerProperties(PlayerModel player) {
        if (player == null) {
            return null;
        }
        return playerProperties.getOrDefault(player, Collections.emptyList());
    }

    public PlayerModel getOwner(final PropertyModel property) {
        return propertyOwner.getOrDefault(property, null);
    }

    public void setOwner(final PropertyModel property, final PlayerModel player) {
        if (player != null && property != null) {
            removeOwner(property);
            playerProperties.computeIfAbsent(player, k -> new ArrayList<>());
            List<PropertyModel> list = playerProperties.get(player);
            if (!list.contains(property)) {
                list.add(property);
                notifyPlayerObservers(player);
            }
            propertyOwner.put(property, player);
        } else if (property != null) {
            removeOwner(property);
        }
    }

    public PlayerModel removeOwner(final PropertyModel property) {
        if (property == null) {
            return null;
        }
        PlayerModel player = propertyOwner.remove(property);
        if (player != null) {
            playerProperties.computeIfPresent(player, (playerModel, propertyModels) -> {
                propertyModels.remove(property);
                return propertyModels;
            });
            notifyPlayerObservers(player);
        }
        return player;
    }

    public List<PropertyModel> removeAllPlayerProperties(PlayerModel player) {
        List<PropertyModel> removedProperties = playerProperties.remove(player);
        if (removedProperties != null) {
            for (PropertyModel property : removedProperties) {
                propertyOwner.remove(property);
            }
        }
        notifyPlayerObservers(player);
        return removedProperties;
    }

    public List<PropertyModel> getCategoryProperties(PropertyCategory category) {
        return propertyCategory.get(category);
    }

    private void notifyPlayerObservers(PlayerModel player) {
        for (Observer<PlayerModel> observer : ownerObservers) {
            observer.notify(player);
        }
    }

    public Observable<PlayerModel> getOwnerObservable() {
        return ownerObservable;
    }

    private void registerOwnerObserver(Observer<PlayerModel> observer) {
        if (observer != null) {
            ownerObservers.add(observer);
        }
    }

    private void deregisterOwnerObserver(Observer<PlayerModel> observer) {
        if (observer != null) {
            ownerObservers.remove(observer);
        }
    }
}
