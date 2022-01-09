package model.property;

public class PropertyModel {
    private String name;
    private PropertyCategory category;
    private int price;
    private int houseNumber = 0;
    private int hotelNumber = 0;
    private int housePrice = 0;
    private int hotelPrice = 0;
    private int mortgageValue;
    private int[] rentValue;

    public PropertyModel() {
    }

    public PropertyModel(String name,
                         PropertyCategory category,
                         int price,
                         int houseNumber,
                         int hotelNumber,
                         int housePrice,
                         int hotelPrice,
                         int mortgageValue,
                         int[] rentValue
    ) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.houseNumber = houseNumber;
        this.hotelNumber = hotelNumber;
        this.housePrice = housePrice;
        this.hotelPrice = hotelPrice;
        this.mortgageValue = mortgageValue;
        this.rentValue = rentValue;
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

    public int getMortgageValue() {
        return mortgageValue;
    }

    public int getBaseRent() {
        return rentValue[0];
    }

    public int getRent() {
        return rentValue[houseNumber];
    }

    public int getRent(int houseNumber) {
        return rentValue[houseNumber];
    }

    public boolean isImprovable() {
        return !category.equals(PropertyCategory.RAILROAD) && !category.equals(PropertyCategory.UTILITY);
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setHotelNumber(int hotelNumber) {
        this.hotelNumber = hotelNumber;
    }
}
