package it.monopoly.controller.board;

public class PlayerPosition {
    private final String name;
    private final String id;
    private final String color;
    private final String position;

    public PlayerPosition(String name, String id, String color, String position) {
        this.name = name;
        this.id = id;
        this.color = color;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public String getPosition() {
        return position;
    }
}
