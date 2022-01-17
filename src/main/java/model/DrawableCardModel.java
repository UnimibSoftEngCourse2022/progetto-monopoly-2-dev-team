package model;

import model.enums.*;

public class DrawableCardModel {

    private String text;
    private DrawableCardCategory category;

    // movement commands
    private Movement movement;
    private int where;
    private boolean direct;

    // pay commands
    private Pay pay;
    private int amount;
    private int houseFee;
    private int hotelFee;

    // payments commands
    private Creditor creditor;
    private Debtor debtor;

    // out of jail free commands
    private boolean keep;


    public String getText() {
        return text;
    }

    public DrawableCardCategory getCategory() {
        return category;
    }

    public Movement getMovement() {
        return movement;
    }

    public int getWhere() {
        return where;
    }

    public boolean isDirect() {
        return direct;
    }

    public Pay getPay() {
        return pay;
    }

    public int getAmount() {
        return amount;
    }

    public int getHouseFee() {
        return houseFee;
    }

    public int getHotelFee() {
        return hotelFee;
    }

    public Creditor getCreditor() {
        return creditor;
    }

    public Debtor getDebtor() {
        return debtor;
    }

    public boolean isKeep() {
        return keep;
    }
}
