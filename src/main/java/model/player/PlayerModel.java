package model.player;

public class PlayerModel {
    private String id;
    private String name;

    public PlayerModel(String id, String name, int funds) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
