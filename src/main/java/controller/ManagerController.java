package controller;

import controller.command.ModelCommand;
import controller.ModelController;

import java.util.List;

public abstract class ManagerController<T, R> extends ModelController<T> {
    public abstract R getManager(T model);
    public abstract List<ModelCommand<T>> getCommands(T model);
}
