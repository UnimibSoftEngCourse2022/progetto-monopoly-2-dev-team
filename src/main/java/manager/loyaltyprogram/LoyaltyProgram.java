package manager.loyaltyprogram;

import model.player.PlayerModel;

public abstract class LoyaltyProgram<T> {

    private T value;
    private final Type type;
    protected PlayerModel creditor;

    public enum Type {
        PERCENTAGE,
        POINTS
    }

    public LoyaltyProgram(PlayerModel creditor, Type type) {

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

    public abstract T getValue();
}
