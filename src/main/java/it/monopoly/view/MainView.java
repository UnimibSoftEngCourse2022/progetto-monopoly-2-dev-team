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
import it.monopoly.controller.Controller;
import it.monopoly.controller.command.Command;
import it.monopoly.manager.AbstractOfferManager;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.player.ReadablePlayerModel;
import it.monopoly.model.property.PropertyModel;
import it.monopoly.util.ViewUtil;
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
    private ReadablePlayerModel readablePlayer;
    private CommandButtonView playerCommandButtonView;
    private HorizontalLayout footer;
    private Button startGameButton;

    private final Map<Class<?>, Observer<?>> observers = new HashMap<>();
    private final Map<Class<?>, Consumer<?>> consumers = new HashMap<>();
    private OffersView offersView;

    public MainView(@Autowired Controller controller) {
        this.controller = controller;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {

        player = controller.setupPlayer(this);
        readablePlayer = controller.getReadablePlayer(player);

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
        auctionButton.addClickListener(event -> controller.startAuction());

        Button sellButton = new Button("Sell");
        sellButton.addClickListener(event -> controller.startSell(player));

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
        footer.add(auctionButton, sellButton);
        footer.add(playerCommandButtonView);
        footer.setWidthFull();
        footer.setHeight(45, Unit.PERCENTAGE);
        footer.setAlignItems(Alignment.END);
        setJustifyContentMode(JustifyContentMode.END);
        footer.setAlignSelf(Alignment.START, propertiesVerticalLayout);
        footer.setAlignSelf(Alignment.END, playerCommandButtonView);

        add(footer);
    }

    private void notifyOffers(AbstractOfferManager offerManager) {
        ViewUtil.runOnUiThread(getUI(), () -> {
            if (offerManager.hasEnded()) {
                if (offersView != null) {
                    remove(offersView);
                    setButtonActive(readablePlayer.isTurn());
                }
            } else {
                offersView = new OffersView(player, controller.getReadablePlayer(player), offerManager);
                offersView.setAlignSelf(Alignment.CENTER);
                offersView.getStyle().set("position", "absolute");
                setButtonActive(false);
                add(offersView);
            }
        });
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
        if (AbstractOfferManager.class.equals(className)) {
            consumer = auctionManager -> MainView.this.notifyOffers((AbstractOfferManager) auctionManager);
        }

        if (consumer != null) {
            consumers.put(className, consumer);
        }
        return consumer;
    }

    public void updateReadable(ReadablePlayerModel readablePlayer) {
        this.readablePlayer = readablePlayer;
        propertyListView.setProperties(readablePlayer.getProperties());
        setButtonActive(readablePlayer.isTurn());
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
