package it.monopoly.model;

import it.monopoly.model.enums.OfferType;
import it.monopoly.model.player.PlayerModel;

public class OfferModel {

    private final OfferType type;
    private final PlayerModel player;
    private final int amount;

    public OfferModel(OfferType type, PlayerModel player, int amount) {
        this.type = type;
        this.player = player;
        this.amount = amount;
    }

    public OfferType getType() {
        return type;
    }

    public PlayerModel getPlayer() {
        return player;
    }

    public int getAmount() {
        return amount;
    }
}
