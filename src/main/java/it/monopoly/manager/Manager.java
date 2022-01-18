package it.monopoly.manager;

public abstract class Manager<T> {
    protected final T model;

    protected Manager(T model) {
        this.model = model;
    }

    public T getModel() {
        return model;
    }
}
