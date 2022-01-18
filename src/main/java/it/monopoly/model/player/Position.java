package it.monopoly.model.player;

public class Position {
    private int intPosition;
    private PlayerMovement lastPlayerMovement;

    public Position() {
        this.intPosition = 0;
        lastPlayerMovement = new PlayerMovement(0, 0, false);
    }

    public int getIntPosition() {
        return intPosition;
    }

    public PlayerMovement getLastMovement() {
        return lastPlayerMovement;
    }

    public void setPosition(int newPosition) {
        setPosition(newPosition, false);
    }

    public void setPosition(int newPosition, boolean direct) {
        lastPlayerMovement = new PlayerMovement(this.intPosition, newPosition, direct);
        this.intPosition = newPosition;
    }
}
