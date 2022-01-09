package model.player;

public class Position {
    private int intPosition;
    private Movement lastMovement;

    public Position() {
        this.intPosition = 0;
        lastMovement = new Movement(0, 0, false);
    }

    public int getPosition() {
        return intPosition;
    }

    public Movement getLastMovement() {
        return lastMovement;
    }

    protected void setPosition(int newPosition) {
        setPosition(newPosition, false);
    }

    protected void setPosition(int newPosition, boolean direct) {
        lastMovement = new Movement(this.intPosition, newPosition, direct);
        this.intPosition = newPosition;
    }
}
