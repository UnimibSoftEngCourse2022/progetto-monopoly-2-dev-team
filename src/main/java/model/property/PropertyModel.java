package model.property;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PropertyModel {
    private String name;
    private PropertyCategory category;
    private int price;
    @JsonIgnore
    private int houseNumber = 0;
    @JsonIgnore
    private int hotelNumber = 0;
    private int housePrice = 0;
    private int hotelPrice = 0;
    private int mortgageValue;
    @JsonIgnore
    private boolean mortgaged;
    private int[] rentValue;

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

    public boolean isMortgaged() {
        return mortgaged;
    }

    public int[] getRentValue() {
        return rentValue;
    }

    public int getBaseRent() {
        return rentValue[0];
    }

    public int getHouseRent() {
        return getHouseRent(houseNumber);
    }

    public int getHouseRent(int houseNumber) {
        if (!isImprovable() || houseNumber < 0 || houseNumber > 4) {
            return 0;
        }
        return rentValue[houseNumber];
    }

    public int getHotelRent() {
        if (!isImprovable()) {
            return 0;
        }
        return rentValue[5];
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
