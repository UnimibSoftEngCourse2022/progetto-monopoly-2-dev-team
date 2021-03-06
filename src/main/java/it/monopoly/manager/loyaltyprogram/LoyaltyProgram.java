package it.monopoly.manager.loyaltyprogram;

import it.monopoly.model.player.PlayerModel;

public abstract class LoyaltyProgram {

    private final Type type;
    protected PlayerModel creditor;

    public enum Type {
        PERCENTAGE,
        POINTS
    }

    protected LoyaltyProgram(PlayerModel creditor, Type type) {
        this.creditor = creditor;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public PlayerModel getCreditor() {
        return creditor;
    }

    public abstract void gatherSales(PlayerModel player, int price);

    public abstract int spendSales(PlayerModel player, int price);

    public abstract String getValue();
}
