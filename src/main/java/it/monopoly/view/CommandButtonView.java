package it.monopoly.view;


import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import it.monopoly.controller.command.Command;
import it.monopoly.util.ViewUtil;

import java.util.List;

public class CommandButtonView extends HorizontalLayout {
    private transient List<Command> commands = null;
    private boolean active = false;

    public CommandButtonView() {
        this(null);
    }

    public CommandButtonView(List<Command> commands) {
        newCommands(commands);
    }

    public void newCommands(List<Command> commands) {
        this.commands = commands;
        refreshButtons();
    }

    public void setActive(boolean active) {
        this.active = active;
        ViewUtil.runOnUiThread(getUI(), this::refreshButtons);
    }

    public void refreshButtons() {
        if (commands != null) {
            clear();
            for (Command command : commands) {
                if (command != null) {
                    Button button = new Button(command.getCommandName());
                    button.getElement().getStyle().set("margin", "auto");
                    button.setEnabled(active && command.isEnabled());
                    button.addClickListener(
                            (ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> command.execute()
                    );
                    add(button);
                }
            }
        }
    }

    public void clear() {
        removeAll();
    }
}
