package model.player;

public class Position {
    private int position;
    private Movement lastMovement;

    public Position() {
        this.position = 0;
        lastMovement = new Movement(0, 0, false);
    }

    public int getPosition() {
        return position;
    }

    public Movement getLastMovement() {
        return lastMovement;
    }

    protected void setPosition(int newPosition) {
        setPosition(newPosition, false);
    }

    protected void setPosition(int newPosition, boolean direct) {
        lastMovement = new Movement(this.position, newPosition, direct);
        this.position = newPosition;
    }
}
