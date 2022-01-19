package it.monopoly.view;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.router.Route;
import it.monopoly.Controller;
import it.monopoly.controller.command.Command;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.player.ReadablePlayerModel;
import it.monopoly.model.property.PropertyModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Push
@Route("")
public class MainView extends VerticalLayout implements Observer<ReadablePlayerModel> {
    private final Controller controller;
    private final CommandButtonView propertyCommandButtonView;
    private final PropertyListView propertyListView;
    private final PlayerModel player;

    public MainView(@Autowired Controller controller) {
        this.controller = controller;
        player = controller.startPlayer(this);
        propertyListView = new PropertyListView(
                (SelectionListener<Grid<PropertyModel>, PropertyModel>) selectionEvent -> selectionEvent
                        .getFirstSelectedItem()
                        .ifPresent(MainView.this::displayCommands)
        );
        propertyListView.setProperties(controller.getProperties());

        propertyCommandButtonView = new CommandButtonView();

        Div div = new Div();
        div.add(propertyListView, propertyCommandButtonView);
        div.setWidth(30, Unit.PERCENTAGE);
        div.setHeight(20, Unit.PERCENTAGE);

        CommandButtonView playerCommandButtonView = new CommandButtonView(controller.getCommandController().generateCommands(player));

        controller.registerObservable(player, this);
        Button newPropertyButton = new Button("New Property");
        newPropertyButton.addClickListener(listener -> controller.addProperty(player));

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.EVENLY);
        setMargin(false);
        setSpacing(false);
        setPadding(false);

        add(div, playerCommandButtonView, newPropertyButton);
    }

    private void displayCommands(PropertyModel property) {
        List<Command> commands = controller.getCommandController().generateCommands(property);
        propertyCommandButtonView.newCommands(commands);
    }

    @Override
    public void notify(ReadablePlayerModel player) {
        propertyListView.setProperties(player.getProperties());
    }
}
