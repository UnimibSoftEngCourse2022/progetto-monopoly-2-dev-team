package it.monopoly.model;

import it.monopoly.manager.loyaltyprogram.LoyaltyProgram;

public class Configuration {

    private final int playerNumber;
    private final int playerFund;
    private final float randomnessIndex;
    private final float taxRate;
    private final float priceChangeRate;
    private final LoyaltyProgram.Type loyaltyType;

    public Configuration(int playerNumber,
                         int playerFund,
                         float randomnessIndex,
                         float taxRate,
                         float priceChangeRate,
                         LoyaltyProgram.Type loyaltyType) {
        this.playerNumber = playerNumber;
        this.playerFund = playerFund;
        this.randomnessIndex = randomnessIndex;
        this.taxRate = taxRate;
        this.priceChangeRate = priceChangeRate;
        this.loyaltyType = loyaltyType;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public int getPlayerFund() {
        return playerFund;
    }

    public float getRandomnessIndex() {
        return randomnessIndex;
    }

    public float getTaxRate() {
        return taxRate;
    }

    public float getPriceChangeRate() {
        return priceChangeRate;
    }

    public LoyaltyProgram.Type getLoyaltyType() {
        return loyaltyType;
    }
}
