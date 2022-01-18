package controller;

import controller.command.Command;
import manager.Manager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ManagerController<T, R extends Manager<T>> extends ModelController<T> {
    protected Map<T, R> modelToManagerMap = new ConcurrentHashMap<>();

    public R getManager(T model) {
        return modelToManagerMap.getOrDefault(model, null);
    }
}
