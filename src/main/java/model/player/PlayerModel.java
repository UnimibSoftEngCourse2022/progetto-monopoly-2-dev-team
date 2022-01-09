package model.player;

public class PlayerModel {
    private String id;
    private String name;
    private int funds;
    private PlayerState state;
    private Position position;

    public PlayerModel(String id, String name, int funds) {
        this.id = id;
        this.name = name;
        this.funds = funds;
        this.state = PlayerState.FREE;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getFunds() {
        return funds;
    }

    public PlayerState getState() {
        return state;
    }

    public Position getPosition() {
        return position;
    }

    public void setFunds(int funds) {
        this.funds = funds;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public void setPosition(int position) {
        this.position.setPosition(position);
    }

    public void setDirectPosition(int position) {
        this.position.setPosition(position, true);
    }
}
