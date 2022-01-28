package it.monopoly.view;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import it.monopoly.controller.Controller;
import it.monopoly.controller.RouteController;
import it.monopoly.controller.board.PlayerPosition;
import it.monopoly.controller.command.Command;
import it.monopoly.controller.event.callback.BuyOrAuctionCallback;
import it.monopoly.manager.AbstractOfferManager;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.player.PlayerState;
import it.monopoly.model.player.ReadablePlayerModel;
import it.monopoly.model.property.ReadablePropertyModel;
import it.monopoly.util.ViewUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static it.monopoly.view.WelcomeView.PLAYER_NAME;

@Push
@Route(value = ":id(([0-9]|[a-z]){16})")
public class MainView extends HorizontalLayout implements BeforeEnterObserver {
    private final Logger logger = LogManager.getLogger(getClass());
    private final RouteController routeController;
    private Controller controller;
    private final Map<Class<?>, Observer<?>> observers = new HashMap<>();
    private final Map<Class<?>, Consumer<?>> consumers = new HashMap<>();
    private CommandButtonView propertyCommandButtonView;
    private PropertyListView propertyListView;
    private PlayerModel player;
    private ReadablePlayerModel readablePlayer;
    private CommandButtonView playerCommandButtonView;
    private HorizontalLayout testCommandsLayout;
    private Button startGameButton;
    private OffersView offersView;
    private PlayerListView playerListView;
    private transient Consumer<ReadablePlayerModel> allPlayersConsumer;
    private transient Consumer<String> messageConsumer;
    private BoardView boardView;
    private String id;

    public MainView(@Autowired RouteController routeController) {
        this.routeController = routeController;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        beforeEnterEvent.getRouteParameters().get("id").ifPresent(s -> {
            id = s.toLowerCase();
            controller = routeController.getController(id);
        });
        if (controller == null) {
            beforeEnterEvent.forwardTo(WelcomeView.class);
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        String js = "window.onbeforeunload = function () {" +
                "element.$server.closeSession();" +
                "};";

        getElement().executeJs(js);

        getElement().executeJs("element = $0", getElement());


        VerticalLayout leftLayout = new VerticalLayout();

        propertyListView = new PropertyListView(
                (SelectionListener<Grid<ReadablePropertyModel>, ReadablePropertyModel>) selectionEvent -> selectionEvent
                        .getFirstSelectedItem()
                        .ifPresentOrElse(MainView.this::displayCommands, () -> propertyCommandButtonView.clear())
        );
        propertyCommandButtonView = new CommandButtonView();
        VerticalLayout propertiesVerticalLayout = new VerticalLayout();
        propertiesVerticalLayout.setMargin(false);
        propertiesVerticalLayout.setHeight(50, Unit.PERCENTAGE);

        playerListView = new PlayerListView();
        playerListView.setHeight(50, Unit.PERCENTAGE);
        updateAllPlayers(readablePlayer);

        propertiesVerticalLayout.add(propertyListView, propertyCommandButtonView);

        leftLayout.add(playerListView, propertiesVerticalLayout);

        VerticalLayout rightLayout = new VerticalLayout();

        playerCommandButtonView = new CommandButtonView();//controller.getCommandController().generateCommands(player));
        playerCommandButtonView.setJustifyContentMode(JustifyContentMode.AROUND);
        playerCommandButtonView.setWidthFull();

        boardView = new BoardView();
        rightLayout.add(boardView);

        startGameButton = null;
        String margin = "margin";
        String auto = "auto";
        if (!controller.isGameStarted()) {
            startGameButton = new Button("Start Game");
            startGameButton.getElement().getStyle().set(margin, auto);
            startGameButton.addClickListener(listener -> startGame());
        }

        Button newPropertyButton = new Button("New Property");
        newPropertyButton.getElement().getStyle().set(margin, auto);
        newPropertyButton.addClickListener(listener -> controller.addProperty(player));

        Button auctionButton = new Button("Auction");
        auctionButton.getElement().getStyle().set(margin, auto);
        auctionButton.addClickListener(event -> controller.startAuction(player));

        Button sellButton = new Button("Sell");
        sellButton.getElement().getStyle().set(margin, auto);
        sellButton.addClickListener(event -> controller.startSell(player));

        NumberField goToField = new NumberField("Go to");
        goToField.setMin(0);
        goToField.setMax(40);
        goToField.addKeyDownListener(
                Key.ENTER,
                (ComponentEventListener<KeyDownEvent>) keyDownEvent
                        -> {
                    if (goToField.getValue() != null) {
                        controller.getPlayerController().getManager(player).moveTo(goToField.getValue().intValue(), false);
                    }
                }
        );

        testCommandsLayout = new HorizontalLayout();
        if (startGameButton != null) {
            testCommandsLayout.add(startGameButton);
        }
        testCommandsLayout.add(newPropertyButton, auctionButton, sellButton, goToField);
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

        logger.info("Attached " + this);
        if (player == null) {
            String playerName = String.valueOf(ComponentUtil.getData(attachEvent.getUI(), PLAYER_NAME));
            player = controller.setupPlayer(playerName, this);
        }
        readablePlayer = controller.getReadablePlayer(player);
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

    public void showBuyPropertyDialog(ReadablePropertyModel property, BuyOrAuctionCallback callback) {
        BuyOrAuctionView dialog = new BuyOrAuctionView(property, callback);
        showAndRemoveDialog(dialog);
    }

    public void showOkDialog(String message) {
        OkDialog dialog = new OkDialog(message);
        showAndRemoveDialog(dialog);
    }

    private void showAndRemoveDialog(Dialog dialog) {
        ViewUtil.runOnUiThread(getUI(), () -> {
            add(dialog);
            dialog.addDialogCloseActionListener(e -> MainView.this.remove(dialog));
            dialog.open();
        });
    }


    @SuppressWarnings(value = "unchecked")
    public <T> Observer<T> getObserver(Class<T> className) {
        if (observers.containsKey(className)) {
            return (Observer<T>) observers.get(className);
        }

        Observer<T> observer = null;
        if (ReadablePlayerModel.class.equals(className)) {
            observer = obj -> updatePlayer((ReadablePlayerModel) obj);
        } else if (PlayerPosition.class.equals(className)) {
            observer = obj -> boardView.updatePosition((PlayerPosition) obj);
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

    public Consumer<String> getMessageConsumer() {
        if (messageConsumer == null) {
            messageConsumer = this::publishNotification;
        }
        return messageConsumer;
    }

    public void publishNotification(String message) {
        ViewUtil.runOnUiThread(getUI(), () -> {
            Notification notification = new Notification(message);
            notification.setPosition(Notification.Position.TOP_END);
            notification.setDuration(2000);
            add(notification);
            notification.open();
        });
    }

    public Consumer<ReadablePlayerModel> getAllPlayerConsumer() {
        if (allPlayersConsumer == null) {
            allPlayersConsumer = this::updateAllPlayers;
        }
        return allPlayersConsumer;
    }

    public void updatePlayer(ReadablePlayerModel readablePlayer) {
        this.readablePlayer = readablePlayer;
        ViewUtil.runOnUiThread(getUI(), () -> {
            getPlayerCommands();
            getPropertiesAndUpdate();
            setButtonActive(!PlayerState.BANKRUPT.equals(readablePlayer.getState()) &&
                    readablePlayer.isTurn());
            setJustifyContentMode(JustifyContentMode.END);
        });
    }

    private void getPlayerCommands() {
        ViewUtil.runOnUiThread(getUI(), () -> {
            List<Command> commands = controller.getCommandController().generateCommands(player);
            playerCommandButtonView.newCommands(commands);
        });
    }

    private void getPropertiesAndUpdate() {
        List<ReadablePropertyModel> properties = controller.getReadableProperties(player);
        ViewUtil.runOnUiThread(getUI(), () -> {
            propertyCommandButtonView.clear();
            propertyListView.setProperties(properties);
        });
    }

    private void setButtonActive(boolean active) {
        playerCommandButtonView.setActive(active);
        propertyCommandButtonView.setActive(active);
    }

    public void updateAllPlayers(ReadablePlayerModel players) {
        List<ReadablePlayerModel> list = controller.getPlayers();
        ViewUtil.runOnUiThread(getUI(), () -> playerListView.setPlayers(list));
    }

    @ClientCallable
    public void closeSession() {
        logger.info("Closing " + this);
        controller.closePlayerSession(player);
    }

    private void startGame() {
        controller.startGame();
        testCommandsLayout.remove(startGameButton);
    }

    private void displayCommands(ReadablePropertyModel property) {
        List<Command> commands = controller.getCommandController().generateCommands(property.getModel());
        propertyCommandButtonView.newCommands(commands);
    }
}
