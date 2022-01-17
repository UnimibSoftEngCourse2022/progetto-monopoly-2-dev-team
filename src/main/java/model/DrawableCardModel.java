package model;

import model.enums.*;

public class DrawableCardModel {

    private String text;
    private DrawableCardCategory category;

    private Movement movement;
    private int where;
    private boolean direct;

    private Pay pay;
    private int amount;
    private int houseFee;
    private int hotelFee;

    private Creditor creditor;
    private Debtor debtor;

    private boolean keep;






    public String getText() {
        return text;
    }

    public DrawableCardCategory getCategory() {
        return category;
    }
    

}
