package it.monopoly.controller.board;

public class PlayerPosition {
    private final String name;
    private final String id;
    private final String position;

    public PlayerPosition(String name, String id, String position) {
        this.name = name;
        this.id = id;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPosition() {
        return position;
    }
}
