package it.monopoly.controller;

import java.util.ArrayList;
import java.util.List;

public abstract class ModelController<T> {
    protected List<T> models = new ArrayList<>();

    public List<T> getModels() {
        return models;
    }
}
