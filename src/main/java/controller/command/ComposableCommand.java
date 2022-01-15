package controller.command;

import java.util.List;

public abstract class ComposableCommand implements Command{
    protected List<Command> commands;

    public void addCommand(Command command) {
        commands.add(command);
    }

    public void execute() {
        for (Command command : commands) {
            command.execute();
        }
    }
}
