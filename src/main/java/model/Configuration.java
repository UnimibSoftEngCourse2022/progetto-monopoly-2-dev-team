package model;

public class Configuration {

    private final int playerNumber;
    private final int playerFund;
    private final float randomnessIndex;
    private final float taxRate;
    private final float priceChangeRate;

    public Configuration(int playerNumber,
                         int playerFund,
                         float randomnessIndex,
                         float taxRate,
                         float priceChangeRate) {
        this.playerNumber = playerNumber;
        this.playerFund = playerFund;
        this.randomnessIndex = randomnessIndex;
        this.taxRate = taxRate;
        this.priceChangeRate = priceChangeRate;
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
}
