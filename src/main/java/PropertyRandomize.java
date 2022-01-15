public class PropertyRandomize {

    private final float pricePercentage;
    private final float mortgagePercentage;
    private final float housePricePercentage;
    private final float hotelPricePercentage;
    private final float rentPercentage;

    public PropertyRandomize() {
        this(randomValue(),
                randomValue(),
                randomValue(),
                randomValue(),
                randomValue());
    }

    public PropertyRandomize(float pricePercentage,
                             float mortgagePercentage,
                             float housePricePercentage,
                             float hotelPricePercentage,
                             float rentPercentage) {
        this.pricePercentage = pricePercentage;
        this.mortgagePercentage = mortgagePercentage;
        this.housePricePercentage = housePricePercentage;
        this.hotelPricePercentage = hotelPricePercentage;
        this.rentPercentage = rentPercentage;
    }

    public float getPricePercentage() {
        return pricePercentage;
    }

    public float getMortgagePercentage() {
        return mortgagePercentage;
    }

    public float getHousePricePercentage() {
        return housePricePercentage;
    }

    public float getHotelPricePercentage() {
        return hotelPricePercentage;
    }

    public float getRentPercentage() {
        return rentPercentage;
    }

    private static float randomValue() {
        return (float) Math.random() * 2 - 1;
    }
}
