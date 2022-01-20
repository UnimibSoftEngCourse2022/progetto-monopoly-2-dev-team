package it.monopoly.model;

import it.monopoly.model.player.PlayerModel;

public class AuctionModel {

    private final PlayerModel player;
    private final int amount;

    public AuctionModel(PlayerModel player, int amount) {
        this.player = player;
        this.amount = amount;
    }

    public PlayerModel getPlayer() {
        return player;
    }

    public int getAmount() {
        return amount;
    }
}
