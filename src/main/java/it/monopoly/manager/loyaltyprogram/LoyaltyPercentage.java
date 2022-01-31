package it.monopoly.manager.loyaltyprogram;

import it.monopoly.model.player.PlayerModel;

public class LoyaltyPercentage extends LoyaltyProgram {

    private static final float MAX_PERCENTAGE = 0.25F;
    private final float randomnessIndex;
    private float percentage = 0.05F;


    public LoyaltyPercentage(PlayerModel creditor) {
        this(creditor, 0F);
    }

    public LoyaltyPercentage(PlayerModel creditor, float randomnessIndex) {
        super(creditor, Type.PERCENTAGE);
        this.randomnessIndex = randomnessIndex;
    }

    @Override
    public void gatherSales(PlayerModel creditor, int price) {
        if (creditor != null && creditor.equals(this.creditor) && percentage < MAX_PERCENTAGE) {

            if (randomnessIndex == 0F) {
                percentage += price * 0.01F;
            } else {
                percentage += price * (randomnessIndex * 0.1F);
            }
        }

        if (percentage > MAX_PERCENTAGE) {
            percentage = MAX_PERCENTAGE;
        }
    }

    @Override
    public int spendSales(PlayerModel creditor, int price) {
        if (creditor != null && creditor.equals(this.creditor)) {
            return price - (int) (price * percentage);
        }

        return price;
    }

    @Override
    public String getValue() {
        return null;
    }
}
