package it.monopoly.model.property;

public class ReadablePropertyModel {
    private final PropertyModel model;
    private final int price;
    private final int housePrice;
    private final int hotelPrice;
    private final int mortgageValue;
    private final String rent;

    public ReadablePropertyModel(PropertyModel property,
                                 int price,
                                 int housePrice,
                                 int hotelPrice,
                                 int mortgageValue,
                                 String rent) {
        this.model = property;
        this.price = price;
        this.housePrice = housePrice;
        this.hotelPrice = hotelPrice;
        this.mortgageValue = mortgageValue;
        this.rent = rent;
    }

    public PropertyModel getModel() {
        return model;
    }

    public String getName() {
        return model.getName();
    }

    public PropertyCategory getCategory() {
        return model.getCategory();
    }

    public int getPrice() {
        return price;
    }

    public int getHouseNumber() {
        return model.getHouseNumber();
    }

    public int getHotelNumber() {
        return model.getHotelNumber();
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
        return model.isMortgaged();
    }

    public String getRent() {
        return rent;
    }

    @Override
    public String toString() {
        return "ReadablePropertyModel{" +
                "name=" + getName() +
                ", price=" + price +
                ", housePrice=" + housePrice +
                ", hotelPrice=" + hotelPrice +
                ", mortgageValue=" + mortgageValue +
                ", rent=" + rent +
                '}';
    }
}
