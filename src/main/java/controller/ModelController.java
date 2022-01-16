package controller;

import controller.command.Command;
import controller.command.ModelCommand;

import java.util.List;

public abstract class ModelController<T> {
    public abstract List<T> getModels();
    public abstract List<Command> getCommands(T model);
}
