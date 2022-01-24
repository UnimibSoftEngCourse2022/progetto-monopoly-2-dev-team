package manager.loyaltyprogram;

import model.player.PlayerModel;

public class LoyaltyPoints extends LoyaltyProgram {

    private final int MAX_POINTS = 200;
    private int points = 0;

    public LoyaltyPoints(PlayerModel playerModel) {
        super(playerModel, Type.POINTS);
    }

    @Override
    public void gatherSales(PlayerModel playerModel, int price) {
        if (playerModel.equals(creditor) && points < MAX_POINTS) {
            points += price * 0.01F;
        }
        if (points > MAX_POINTS) {
            points = MAX_POINTS;
        }
    }

    @Override
    public int spendSales(PlayerModel playerModel, int price) {
        if (playerModel.equals(creditor)) {
            int priceSaled = price - points;
            points = 0;
            return priceSaled;
        }
        return price;
    }
}
