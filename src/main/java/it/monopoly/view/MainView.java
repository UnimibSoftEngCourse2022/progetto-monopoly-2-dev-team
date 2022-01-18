package it.monopoly.view;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.router.Route;
import it.monopoly.Controller;
import it.monopoly.controller.command.Command;
import it.monopoly.model.property.PropertyModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Push
@Route("")
public class MainView extends Div {
    private final Controller controller;

    public MainView(@Autowired Controller controller) {
        this.controller = controller;
        Grid<PropertyModel> grid = new Grid<>(PropertyModel.class, false);
        grid.addColumn(PropertyModel::getName).setHeader("Street name");
        grid.addColumn(PropertyModel::getPrice).setHeader("Price");
        grid.addColumn(PropertyModel::getMortgageValue).setHeader("Mortgage");

        grid.setItems(controller.getProperties());
        grid.addSelectionListener(
                (SelectionListener<Grid<PropertyModel>, PropertyModel>) selectionEvent ->
                        selectionEvent
                                .getFirstSelectedItem()
                                .ifPresent(this::displayCommands)
        );
        add(grid);
    }

    private void displayCommands(PropertyModel property) {
        List<Command> commands = controller.getCommandController().generateCommands(property);
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
