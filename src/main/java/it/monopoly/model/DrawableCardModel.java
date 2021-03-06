package it.monopoly.model;

import it.monopoly.model.enums.CreditorDebtor;
import it.monopoly.model.enums.DrawableCardCategory;
import it.monopoly.model.enums.Movement;
import it.monopoly.model.enums.Pay;

public class DrawableCardModel {

    private String text;
    private DrawableCardCategory category;

    // movement commands
    private Movement movement;
    private int where;
    private boolean direct;
    private int[] near;

    // pay commands
    private Pay pay;
    private int amount;
    private int houseFee;
    private int hotelFee;

    // payments commands
    private CreditorDebtor creditor;
    private CreditorDebtor debtor;

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

    public int[] getNear() {
        return near;
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

    public CreditorDebtor getCreditor() {
        return creditor;
    }

    public CreditorDebtor getDebtor() {
        return debtor;
    }

    public boolean isKeep() {
        return keep;
    }
}
