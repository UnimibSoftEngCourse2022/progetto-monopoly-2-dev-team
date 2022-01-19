package it.monopoly.view;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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

        String js = "window.addEventListener(\"beforeunload\", function () {" +
                "element.$server.closeSession();" +
                "});";

        getElement().executeJs(js);

        js = "window.addEventListener(\"unload\", function () {" +
                "element.$server.closeSession();" +
                "});";

        getElement().executeJs(js);
        getElement().executeJs("element = $0", getElement());

        propertyListView = new PropertyListView(
                (SelectionListener<Grid<PropertyModel>, PropertyModel>) selectionEvent -> selectionEvent
                        .getFirstSelectedItem()
                        .ifPresent(MainView.this::displayCommands)
        );

        propertyCommandButtonView = new CommandButtonView();

        VerticalLayout propertiesVerticalLayout = new VerticalLayout();
        propertiesVerticalLayout.add(propertyListView, propertyCommandButtonView);
        propertiesVerticalLayout.setWidth(35, Unit.PERCENTAGE);

        CommandButtonView playerCommandButtonView =
                new CommandButtonView(controller.getCommandController().generateCommands(player));

        Button newPropertyButton = new Button("New Property");
        newPropertyButton.addClickListener(listener -> controller.addProperty(player));

        setSizeFull();
        setMargin(false);
        setSpacing(false);
        setPadding(false);

        expand(propertyListView);

        HorizontalLayout footer = new HorizontalLayout();
        footer.add(propertiesVerticalLayout, newPropertyButton, playerCommandButtonView);
        footer.setWidthFull();
        footer.setHeight(20, Unit.PERCENTAGE);
        footer.setAlignItems(Alignment.END);
//        footer.setJustifyContentMode(JustifyContentMode.AROUND);
        footer.setAlignSelf(Alignment.START, propertiesVerticalLayout);
        footer.setAlignSelf(Alignment.END, playerCommandButtonView);

        add(footer);
    }

    private void displayCommands(PropertyModel property) {
        List<Command> commands = controller.getCommandController().generateCommands(property);
        propertyCommandButtonView.newCommands(commands);
    }

    @Override
    public void notify(ReadablePlayerModel player) {
        propertyListView.setProperties(player.getProperties());
        setJustifyContentMode(JustifyContentMode.EVENLY);
    }

    @ClientCallable
    public void closeSession() {
        controller.closePlayerSession(player);
    }
}
