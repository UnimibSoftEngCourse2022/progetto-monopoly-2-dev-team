package it.monopoly.model;

import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyCategory;
import it.monopoly.model.property.PropertyModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PropertyMapper implements PropertyOwnerMapper, PropertyCategoryMapper {
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
            playerProperties.computeIfAbsent(player, k -> new ArrayList<>()).add(property);
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
        }
        return player;
    }

    public List<PropertyModel> getCategoryProperties(PropertyCategory category) {
        return propertyCategory.get(category);
    }
}
