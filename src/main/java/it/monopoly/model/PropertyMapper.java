package it.monopoly.model;

import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyCategory;
import it.monopoly.model.property.PropertyModel;
import it.monopoly.view.Observable;
import it.monopoly.view.Observer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PropertyMapper implements PropertyOwnerMapper, PropertyCategoryMapper, Observable<List<PropertyModel>> {

    private final List<Observer<List<PropertyModel>>> observers = new LinkedList<>();

    private final Map<PlayerModel, List<PropertyModel>> playerProperties = new ConcurrentHashMap<>();
    private final Map<PropertyModel, PlayerModel> propertyOwner = new ConcurrentHashMap<>();
    private final Map<PropertyCategory, List<PropertyModel>> propertyCategory = new ConcurrentHashMap<>();

    public PropertyMapper(List<PropertyModel> properties) {
        for (PropertyModel property : properties) {
            propertyCategory.computeIfAbsent(property.getCategory(), k -> new ArrayList<>()).add(property);
        }
    }

    public List<PropertyModel> getPlayerProperties(PlayerModel player) {
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
                notify(player);
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
            notify(player);
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
        notify(player);
        return removedProperties;
    }

    public List<PropertyModel> getCategoryProperties(PropertyCategory category) {
        return propertyCategory.get(category);
    }

    private void notify(PlayerModel player) {
        for (Observer<List<PropertyModel>> observer : observers) {
            observer.notify(getPlayerProperties(player));
        }
    }

    @Override
    public void register(Observer<List<PropertyModel>> observer) {
        observers.add(observer);
    }

    @Override
    public void deregister(Observer<List<PropertyModel>> observer) {
        observers.remove(observer);
    }
}
