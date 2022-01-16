package controller.command;

public abstract class ExecutableModelCommand<T> implements ModelCommand<T>, Command {
    private final T model;

    public ExecutableModelCommand(T model) {
        this.model = model;
    }

    @Override
    public final void execute(T obj) {
        if (isEnabled(obj)) {
            executableMethod(obj);
        }
    }

    public abstract void executableMethod(T obj);

    @Override
    public boolean isEnabled() {
        return model != null && isEnabled(model);
    }

    @Override
    public void execute() {
        if (isEnabled(model)) {
            executableMethod(model);
        }
    }
}
