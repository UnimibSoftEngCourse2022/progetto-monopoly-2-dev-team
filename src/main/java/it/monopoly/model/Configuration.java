package it.monopoly.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import it.monopoly.manager.loyaltyprogram.LoyaltyProgram;

public class Configuration {
    @JsonAlias("player_number")
    private int playerNumber = 6;
    @JsonAlias("player_funds")
    private int playerFund = 1500;
    @JsonAlias("randomness_index")
    private float randomnessIndex = 0;
    @JsonAlias("tax_rate")
    private float taxRate = 0;
    @JsonAlias("price_change_rate")
    private float priceChangeRate = 0;
    @JsonAlias("loyalty_program")
    private LoyaltyProgram.Type loyaltyType = null;

    public Configuration() {
    }

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
