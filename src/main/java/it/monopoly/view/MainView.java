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
import it.monopoly.manager.AuctionManager;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.player.ReadablePlayerModel;
import it.monopoly.model.property.PropertyModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Push
@Route("")
public class MainView extends VerticalLayout {
    private final Controller controller;
    private final CommandButtonView propertyCommandButtonView;
    private final PropertyListView propertyListView;
    private final PlayerModel player;
    private final CommandButtonView playerCommandButtonView;
    private final HorizontalLayout footer;
    private Button startGameButton;

    private final Map<Class<?>, Observer<?>> observers = new HashMap<>();

    public MainView(@Autowired Controller controller) {
        this.controller = controller;
        player = controller.setupPlayer(this);

        //controller.getBroadcaster().registerPlayerListener(MainView.this::notify);

        String js = "window.onbeforeunload = function () {" +
                "element.$server.closeSession();" +
                "};";

        getElement().executeJs(js);

        getElement().executeJs("element = $0", getElement());

        AuctionManager auctionManager = controller.getAuctionManager();
        AuctionView auctionView = new AuctionView(player, controller.getReadablePlayer(player), auctionManager);
        add(auctionView);

        propertyListView = new PropertyListView(
                (SelectionListener<Grid<PropertyModel>, PropertyModel>) selectionEvent -> selectionEvent
                        .getFirstSelectedItem()
                        .ifPresent(MainView.this::displayCommands)
        );

        propertyCommandButtonView = new CommandButtonView();

        VerticalLayout propertiesVerticalLayout = new VerticalLayout();
        propertiesVerticalLayout.add(propertyListView, propertyCommandButtonView);
        propertiesVerticalLayout.setWidth(40, Unit.PERCENTAGE);
        propertiesVerticalLayout.setHeightFull();

        playerCommandButtonView = new CommandButtonView(controller.getCommandController().generateCommands(player));

        Button newPropertyButton = new Button("New Property");
        newPropertyButton.addClickListener(listener -> controller.addProperty(player));

        startGameButton = null;
        if (!controller.isGameStarted()) {
            startGameButton = new Button("Start Game");
            startGameButton.addClickListener(listener -> startGame());
        }

        setSizeFull();
        setMargin(false);
        setSpacing(false);
        setPadding(false);

        expand(propertyListView);

        footer = new HorizontalLayout();
        footer.add(propertiesVerticalLayout, newPropertyButton);
        if (startGameButton != null) {
            footer.add(startGameButton);
        }
        footer.add(playerCommandButtonView);
        footer.setWidthFull();
        footer.setHeight(45, Unit.PERCENTAGE);
        footer.setAlignItems(Alignment.END);
        setJustifyContentMode(JustifyContentMode.END);
        footer.setAlignSelf(Alignment.START, propertiesVerticalLayout);
        footer.setAlignSelf(Alignment.END, playerCommandButtonView);

        add(footer);
    }

    @SuppressWarnings(value = "unchecked")
    public <T> Observer<T> getObserver(Class<T> className) {
        if (observers.containsKey(className)) {
            return (Observer<T>) observers.get(className);
        }

        Observer<T> observer = null;
        if (ReadablePlayerModel.class.equals(className)) {
            observer = obj -> updateReadable((ReadablePlayerModel) obj);
        }

        if (observer != null) {
            observers.put(className, observer);
        }
        return observer;
    }

    public void updateReadable(ReadablePlayerModel player) {
        propertyListView.setProperties(player.getProperties());
        playerCommandButtonView.setActive(player.isTurn());
        propertyCommandButtonView.setActive(player.isTurn());
        setJustifyContentMode(JustifyContentMode.END);
    }

    public void updateAllPlayers(List<ReadablePlayerModel> players) {

    }

    @ClientCallable
    public void closeSession() {
        controller.closePlayerSession(player);
    }

    private void startGame() {
        controller.startGame();
        footer.remove(startGameButton);
    }

    private void displayCommands(PropertyModel property) {
        List<Command> commands = controller.getCommandController().generateCommands(property);
        propertyCommandButtonView.newCommands(commands);
    }
}
