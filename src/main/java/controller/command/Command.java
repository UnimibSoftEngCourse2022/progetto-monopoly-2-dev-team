package controller.command;

public interface Command {
    String getCommandName();

    boolean isEnabled();

    void execute();
}
