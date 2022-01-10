public class PropertyRandomization {

    private final float percentagePrice;
    private final float percentageRent;
    private final float percentageHouse;
    private final float percentageHotel;
    private final float percentageMortgage;

    public PropertyRandomization(float percentagePrice,
                                 float percentageRent,
                                 float percentageHouse,
                                 float percentageHotel,
                                 float percentageMortgage) {
        this.percentagePrice = percentagePrice;
        this.percentageRent = percentageRent;
        this.percentageHouse = percentageHouse;
        this.percentageHotel = percentageHotel;
        this.percentageMortgage = percentageMortgage;
    }

    public float getPercentagePrice() {
        return percentagePrice;
    }

    public float getPercentageRent() {
        return percentageRent;
    }

    public float getPercentageHouse() {
        return percentageHouse;
    }

    public float getPercentageHotel() {
        return percentageHotel;
    }

    public float getPercentageMortgage() {
        return percentageMortgage;
    }


}
