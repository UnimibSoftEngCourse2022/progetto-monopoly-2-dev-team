package it.monopoly.model.player;

import it.monopoly.util.ColorGenerator;

import java.io.Serializable;

public class PlayerModel implements Serializable {
    private final String id;
    private final String name;
    private final String color;

    public PlayerModel(String id, String name) {
        this(id, name, ColorGenerator.getRandomColor());
    }

    public PlayerModel(String id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        PlayerModel that = (PlayerModel) obj;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return getName() + "#" + getId();
    }
}
