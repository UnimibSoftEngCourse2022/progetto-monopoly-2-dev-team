package it.monopoly.model;

import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyModel;

import java.util.List;

public interface PropertyOwnerMapper {

    List<PropertyModel> getPlayerProperties(PlayerModel player);

    PlayerModel getOwner(final PropertyModel property);

    void setOwner(final PropertyModel property, final PlayerModel player);

    PlayerModel removeOwner(final PropertyModel property);

    List<PropertyModel> removeAllPlayerProperties(PlayerModel player);
}
