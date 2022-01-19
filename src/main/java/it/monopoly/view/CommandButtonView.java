package it.monopoly.view;


import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import it.monopoly.controller.command.Command;

import java.util.List;

public class CommandButtonView extends HorizontalLayout {

    public CommandButtonView() {
        this(null);
    }

    public CommandButtonView(List<Command> commands) {
        newCommands(commands);
    }

    public void newCommands(List<Command> commands) {
        if (commands != null) {
            removeAll();
            for (Command command : commands) {
                Button button = new Button(command.getCommandName());
                button.setEnabled(command.isEnabled());
                button.addClickListener(
                        (ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> command.execute()
                );
                add(button);
            }
        }
    }
}
