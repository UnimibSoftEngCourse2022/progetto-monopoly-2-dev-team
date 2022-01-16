package controller;

import controller.command.Command;
import controller.command.ModelCommand;

import java.util.ArrayList;
import java.util.List;

public abstract class ModelController<T> {
    protected List<T> models = new ArrayList<>();

    public List<T> getModels() {
        return models;
    }

    public abstract List<Command> getCommands(T model);
}
