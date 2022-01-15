package controller;

import controller.command.ModelCommand;

import java.util.List;

public abstract class ModelController<T> {
    public abstract List<T> getModels();
    public abstract List<ModelCommand<T>> getCommands(T model);
}
