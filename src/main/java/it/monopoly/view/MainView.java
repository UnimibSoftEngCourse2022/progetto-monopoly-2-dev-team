package it.monopoly.view;

import com.vaadin.flow.component.AttachEvent;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Push
@Route("")
public class MainView extends VerticalLayout {
    private final Controller controller;
    private CommandButtonView propertyCommandButtonView;
    private PropertyListView propertyListView;
    private PlayerModel player;
    private CommandButtonView playerCommandButtonView;
    private HorizontalLayout footer;
    private Button startGameButton;

    private final Map<Class<?>, Observer<?>> observers = new HashMap<>();
    private final Map<Class<?>, Consumer<?>> consumers = new HashMap<>();
    private AuctionView auctionView;

    public MainView(@Autowired Controller controller) {
        this.controller = controller;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {

        player = controller.setupPlayer(this);

        //controller.getBroadcaster().registerPlayerListener(MainView.this::notify);

        String js = "window.onbeforeunload = function () {" +
                "element.$server.closeSession();" +
                "};";

        getElement().executeJs(js);

        getElement().executeJs("element = $0", getElement());

//        startAuction(controller.getAuctionManager());

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

        Button auctionButton = new Button("Auction");
        auctionButton.addClickListener(event -> controller.getEventDispatcher().startAuction(controller.getProperties().get(0)));

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
        footer.add(auctionButton);
        footer.add(playerCommandButtonView);
        footer.setWidthFull();
        footer.setHeight(45, Unit.PERCENTAGE);
        footer.setAlignItems(Alignment.END);
        setJustifyContentMode(JustifyContentMode.END);
        footer.setAlignSelf(Alignment.START, propertiesVerticalLayout);
        footer.setAlignSelf(Alignment.END, playerCommandButtonView);

        add(footer);
    }

    private void notifyAuction(AuctionManager auctionManager) {
        getUI().ifPresent(ui -> ui.access((com.vaadin.flow.server.Command) () -> {
            if (auctionManager.hasEnded()) {
                if (auctionView != null) {
                    remove(auctionView);
                    setButtonActive(true);
                }
            } else {
                auctionView = new AuctionView(player, controller.getReadablePlayer(player), auctionManager);
                auctionView.setAlignSelf(Alignment.CENTER);
                auctionView.getStyle().set("position", "absolute");
                setButtonActive(false);
                add(auctionView);
            }
        }));
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

    @SuppressWarnings(value = "unchecked")
    public <T> Consumer<T> getConsumer(Class<T> className) {
        if (consumers.containsKey(className)) {
            return (Consumer<T>) consumers.get(className);
        }

        Consumer<T> consumer = null;
        if (AuctionManager.class.equals(className)) {
            consumer = auctionManager -> MainView.this.notifyAuction((AuctionManager) auctionManager);
        }

        if (consumer != null) {
            consumers.put(className, consumer);
        }
        return consumer;
    }

    public void updateReadable(ReadablePlayerModel player) {
        propertyListView.setProperties(player.getProperties());
        setButtonActive(player.isTurn());
        setJustifyContentMode(JustifyContentMode.END);
    }

    private void setButtonActive(boolean active) {
        playerCommandButtonView.setActive(active);
        propertyCommandButtonView.setActive(active);
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
