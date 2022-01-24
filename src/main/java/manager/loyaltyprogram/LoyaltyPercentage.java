package manager.loyaltyprogram;

import model.player.PlayerModel;

public class LoyaltyPercentage extends LoyaltyProgram {

    private final float MAX_PERCENTAGE = 0.25F;
    private float percentage = 0.05F;

    public LoyaltyPercentage(PlayerModel playerModel) {
        super(playerModel, Type.PERCENTAGE);
    }

    @Override
    public void gatherSales(PlayerModel playerModel, int price) {
        if (playerModel.equals(creditor) && percentage < MAX_PERCENTAGE) {
            percentage += price * 0.01F;
        }
        if (percentage > MAX_PERCENTAGE) {
            percentage = MAX_PERCENTAGE;
        }
    }

    @Override
    public int spendSales(PlayerModel playerModel, int price) {
        if (playerModel.equals(creditor)) {
            return price - (int) (price * percentage);
        }
        return price;
    }
}
