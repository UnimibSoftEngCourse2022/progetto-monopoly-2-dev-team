package it.monopoly.controller.command;

import java.util.ArrayList;
import java.util.List;

public abstract class ComposableCommand implements Command{
    protected final List<Command> commands = new ArrayList<>();

    public void addCommand(Command command) {
        commands.add(command);
    }

    public void execute() {
        for (Command command : commands) {
            command.execute();
        }
    }
}
