package model.property;

public class PropertyModel {
    private String name;
    private PropertyCategory category;
    private int price;
    private int houseNumber = 0;
    private int hotelNumber = 0;
    private int housePrice = 0;
    private int hotelPrice = 0;
    private int mortgagePrice;
    private int baseRent;
    private int[] houseRent;
    private int hotelRent;

    public PropertyModel(String name,
                         PropertyCategory category,
                         int price,
                         int houseNumber,
                         int hotelNumber,
                         int housePrice,
                         int hotelPrice,
                         int mortgagePrice,
                         int baseRent,
                         int[] houseRent,
                         int hotelRent) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.houseNumber = houseNumber;
        this.hotelNumber = hotelNumber;
        this.housePrice = housePrice;
        this.hotelPrice = hotelPrice;
        this.mortgagePrice = mortgagePrice;
        this.baseRent = baseRent;
        this.houseRent = houseRent;
        this.hotelRent = hotelRent;
    }

    public String getName() {
        return name;
    }

    public PropertyCategory getCategory() {
        return category;
    }

    public int getPrice() {
        return price;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public int getHotelNumber() {
        return hotelNumber;
    }

    public int getHousePrice() {
        return housePrice;
    }

    public int getHotelPrice() {
        return hotelPrice;
    }

    public int getMortgagePrice() {
        return mortgagePrice;
    }

    public int getBaseRent() {
        return baseRent;
    }

    public int getHouseRent() {
        return houseRent[houseNumber];
    }

    public int getHouseRent(int houseNumber) {
        if(houseNumber < 0 || houseNumber > 4) {
            return 0;
        }
        return houseRent[houseNumber];
    }

    public int getHotelRent() {
        return hotelRent;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setHotelNumber(int hotelNumber) {
        this.hotelNumber = hotelNumber;
    }
}
