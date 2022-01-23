package it.monopoly.view;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.router.Route;
import elemental.json.JsonValue;
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
public class MainView extends HorizontalLayout {
    private final Controller controller;
    private CommandButtonView propertyCommandButtonView;
    private PropertyListView propertyListView;
    private PlayerModel player;
    private ReadablePlayerModel readablePlayer;
    private CommandButtonView playerCommandButtonView;
    private HorizontalLayout testCommandsLayout;
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

        String js = "window.onbeforeunload = function () {" +
                "element.$server.closeSession();" +
                "};";

        getElement().executeJs(js);

        getElement().executeJs("element = $0", getElement());


        VerticalLayout leftLayout = new VerticalLayout();

        propertyListView = new PropertyListView(
                (SelectionListener<Grid<PropertyModel>, PropertyModel>) selectionEvent -> selectionEvent
                        .getFirstSelectedItem()
                        .ifPresentOrElse(MainView.this::displayCommands, () -> propertyCommandButtonView.clear())
        );


        propertyCommandButtonView = new CommandButtonView();

        VerticalLayout propertiesVerticalLayout = new VerticalLayout();
        propertiesVerticalLayout.setMargin(false);
        propertiesVerticalLayout.setHeight(50, Unit.PERCENTAGE);
        propertiesVerticalLayout.add(propertyListView, propertyCommandButtonView);

        leftLayout.add(propertiesVerticalLayout);

        VerticalLayout rightLayout = new VerticalLayout();

        BoardView boardView = new BoardView();
        rightLayout.add(boardView);

        playerCommandButtonView = new CommandButtonView(controller.getCommandController().generateCommands(player));
        playerCommandButtonView.setJustifyContentMode(JustifyContentMode.AROUND);
        playerCommandButtonView.setWidthFull();

        startGameButton = null;
        if (!controller.isGameStarted()) {
            startGameButton = new Button("Start Game");
            startGameButton.getElement().getStyle().set("margin", "auto");
            startGameButton.addClickListener(listener -> startGame());
        }

        Button newPropertyButton = new Button("New Property");
        newPropertyButton.getElement().getStyle().set("margin", "auto");
        newPropertyButton.addClickListener(listener -> controller.addProperty(player));

        Button auctionButton = new Button("Auction");
        auctionButton.getElement().getStyle().set("margin", "auto");
        auctionButton.addClickListener(event -> controller.startAuction(player));

        Button sellButton = new Button("Sell");
        sellButton.getElement().getStyle().set("margin", "auto");
        sellButton.addClickListener(event -> controller.startSell(player));

        testCommandsLayout = new HorizontalLayout();
        if (startGameButton != null) {
            testCommandsLayout.add(startGameButton);
        }
        testCommandsLayout.add(newPropertyButton, auctionButton, sellButton);
        testCommandsLayout.setMargin(false);
        testCommandsLayout.setWidthFull();
        testCommandsLayout.setJustifyContentMode(JustifyContentMode.AROUND);
        testCommandsLayout.add(playerCommandButtonView);

        rightLayout.setJustifyContentMode(JustifyContentMode.END);
        rightLayout.add(testCommandsLayout, playerCommandButtonView);

        setSizeFull();
        setSpacing(false);
        setPadding(false);

        add(leftLayout, rightLayout);
        setFlexGrow(.5, leftLayout, rightLayout);
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
                setAlignSelf(Alignment.CENTER, offersView);
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
        ViewUtil.runOnUiThread(getUI(), () -> {
            propertyCommandButtonView.clear();
            propertyListView.setProperties(readablePlayer.getProperties());
            setButtonActive(readablePlayer.isTurn());
            setJustifyContentMode(JustifyContentMode.END);
        });
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
        testCommandsLayout.remove(startGameButton);
    }

    private void displayCommands(PropertyModel property) {
        List<Command> commands = controller.getCommandController().generateCommands(property);
        propertyCommandButtonView.newCommands(commands);
    }
}
