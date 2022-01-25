package manager.loyaltyprogram;

import model.player.PlayerModel;

public class LoyaltyPoints extends LoyaltyProgram {

    private final int MAX_POINTS = 200;
    private final float randomnessIndex;
    private int points = 0;

    public LoyaltyPoints(PlayerModel creditor) {
        this(creditor, 0F);
    }

    public LoyaltyPoints(PlayerModel creditor, float randomnessIndex) {
        super(creditor, Type.POINTS);
        this.randomnessIndex = randomnessIndex;
    }

    @Override
    public void gatherSales(PlayerModel creditor, int price) {
        if (creditor != null && creditor.equals(this.creditor) && points < MAX_POINTS) {

            if (randomnessIndex == 0) {
                points += price * 0.1F;
            } else {
                points += price * (randomnessIndex * 0.1F);
            }
        }

        if (points > MAX_POINTS) {
            points = MAX_POINTS;
        }
    }

    @Override
    public int spendSales(PlayerModel creditor, int price) {
        if (creditor != null && creditor.equals(this.creditor)) {

            int priceSaled;

            if (price >= points) {
                priceSaled = price - points;
                points = 0;
            } else {
                priceSaled = 0;
                points = points - price;
            }

            return priceSaled;
        }

        return price;
    }

    @Override
    public Object getValue() {
        return points;
    }

}
