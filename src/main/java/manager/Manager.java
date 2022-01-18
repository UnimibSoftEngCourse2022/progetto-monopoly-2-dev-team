package manager;

public abstract class Manager<T> {
    protected final T model;

    public Manager(T model) {
        this.model = model;
    }

    public T getModel() {
        return model;
    }
}
