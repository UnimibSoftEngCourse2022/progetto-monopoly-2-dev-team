package controller.command;

public interface ModelCommand<T> {
    String getCommandName();

    boolean isEnabled(T obj);

    void execute(T obj);
}
