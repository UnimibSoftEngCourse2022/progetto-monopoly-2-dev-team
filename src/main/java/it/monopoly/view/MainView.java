package it.monopoly.view;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.*;
import it.monopoly.controller.Controller;
import it.monopoly.controller.RouteController;
import it.monopoly.controller.board.PlayerPosition;
import it.monopoly.controller.command.Command;
import it.monopoly.controller.event.callback.FirstOrSecondCallback;
import it.monopoly.controller.event.callback.FirstSecondChoice;
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

import static it.monopoly.view.WelcomeView.*;

@Push
@Route(value = ":id(([0-9]|[a-z]){16})")
public class MainView extends HorizontalLayout implements BeforeEnterObserver, BeforeLeaveObserver {
    private final Logger logger = LogManager.getLogger(getClass());
    private final RouteController routeController;
    private Controller controller;
    private final Map<Class<?>, Observer<?>> observers = new HashMap<>();
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
    private String playerName;
    private FirstSecondChoiceDialog startGameDialog;

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
            ComponentUtil.setData(beforeEnterEvent.getUI(), ERROR, "Game not found");
            beforeEnterEvent.forwardTo(WelcomeView.class);
            return;
        } else if (controller.maxPlayerReached()) {
            ComponentUtil.setData(beforeEnterEvent.getUI(), ERROR, "Max player number reached");
            beforeEnterEvent.forwardTo(WelcomeView.class);
            return;
        }
        Object playerNameObject = ComponentUtil.getData(beforeEnterEvent.getUI(), PLAYER_NAME);
        if (playerNameObject == null) {
            ComponentUtil.setData(beforeEnterEvent.getUI(), NO_NAME_ID, id);
            ComponentUtil.setData(beforeEnterEvent.getUI(), ERROR, "Set player name");
            beforeEnterEvent.rerouteTo(WelcomeView.class);
            return;
        }
        playerName = String.valueOf(playerNameObject);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        getElement().executeJs("element = $0", getElement());

        String js = "window.onbeforeunload = function () {" +
                "element.$server.closeSession();" +
                "};";
        getElement().executeJs(js);


        VerticalLayout leftLayout = new VerticalLayout();

        propertyListView = new PropertyListView(
                selectionEvent ->
                        selectionEvent
                                .getFirstSelectedItem()
                                .ifPresentOrElse(MainView.this::displayCommands, () -> propertyCommandButtonView.clear())
        );
        propertyCommandButtonView = new CommandButtonView();
        propertyCommandButtonView.setWidthFull();
        VerticalLayout propertiesVerticalLayout = new VerticalLayout();
        propertiesVerticalLayout.setMargin(false);
        propertiesVerticalLayout.setHeight(50, Unit.PERCENTAGE);

        playerListView = new PlayerListView(controller);
        playerListView.setHeight(50, Unit.PERCENTAGE);
        updateAllPlayers(readablePlayer);

        propertiesVerticalLayout.add(propertyListView, propertyCommandButtonView);

        leftLayout.add(playerListView, propertiesVerticalLayout);

        VerticalLayout rightLayout = new VerticalLayout();

        playerCommandButtonView = new CommandButtonView();
        playerCommandButtonView.setJustifyContentMode(JustifyContentMode.AROUND);
        playerCommandButtonView.setWidthFull();


        HorizontalLayout playerButtons = new HorizontalLayout();
        playerButtons.setWidthFull();
        playerButtons.setJustifyContentMode(JustifyContentMode.AROUND
        );
        Button quitButton = new Button("Quit", e -> closeSessionAndQuit());

        playerButtons.add(playerCommandButtonView, quitButton);

        boardView = new BoardView();
        rightLayout.add(boardView);

        //testCommands();
        if (testCommandsLayout != null) {
            rightLayout.add(testCommandsLayout);
        }

        rightLayout.setJustifyContentMode(JustifyContentMode.END);
        rightLayout.add(playerButtons);

        setSizeFull();
        setSpacing(false);
        setPadding(false);

        add(leftLayout, rightLayout);
        setFlexGrow(.5, leftLayout, rightLayout);

        logger.info("Attached {}", this);
        if (player == null) {
            player = controller.setupPlayer(playerName, this);
            if (player == null) {
                attachEvent.getUI().navigate(WelcomeView.class);
                return;
            }
        }
        readablePlayer = controller.getReadablePlayer(player);

        refreshStartDialog();
    }

    private void testCommands() {
        String margin = "margin";
        String auto = "auto";


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
    }

    private void refreshStartDialog() {
        if (!controller.isGameStarted()) {
            if (startGameDialog == null) {

                startGameDialog = new FirstSecondChoiceDialog(
                        choice -> {
                            if (FirstSecondChoice.FIRST.equals(choice)) {
                                startGame();
                            } else {
                                closeSessionAndQuit();
                            }
                        });
                showAndRemoveDialog(startGameDialog);
            }
            ViewUtil.runOnUiThread(getUI(), () -> {
                List<PlayerModel> players = controller.getPlayerModels();
                boolean isCreator = players.indexOf(player) == 0;
                int playerNumber = players.size();
                boolean isMoreThanOnePlayer = playerNumber > 1;
                startGameDialog.setFirstChoice("Start Game", isCreator && isMoreThanOnePlayer);
                String text = "Wait for";
                if (isCreator) {
                    text += " other players to join";
                    if (isMoreThanOnePlayer) {
                        text += " or start the game";
                    }
                } else {
                    text += " creator to start the game";
                }

                text += " | Code: " + id;
                text += " | Players: " + playerNumber;

                startGameDialog.setText(text);
                startGameDialog.setSecondChoice("Quit");
            });
        } else if (startGameDialog != null) {
            startGameDialog.close();
            remove(startGameDialog);
        }
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

    public void showOkDialog(String message) {
        OkDialog dialog = new OkDialog(message);
        showAndRemoveDialog(dialog);
    }

    public void showYesNoDialog(
            String text,
            String first,
            String second,
            FirstOrSecondCallback callback
    ) {
        FirstSecondChoiceDialog dialog = new FirstSecondChoiceDialog(callback);
        dialog.setText(text);
        dialog.setFirstChoice(first);
        dialog.setSecondChoice(second);
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

    public Consumer<AbstractOfferManager> getOfferConsumer() {
        return this::notifyOffers;
    }

    public Consumer<PlayerModel> getWinnerConsumer() {
        return this::showWinner;
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
            notification.setDuration(3000);
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
        ViewUtil.runOnUiThread(getUI(), () -> {
            refreshStartDialog();
            playerListView.setPlayers(player, list);
        });
    }

    private void displayCommands(ReadablePropertyModel property) {
        List<Command> commands = controller.getCommandController().generateCommands(property.getModel());
        propertyCommandButtonView.newCommands(commands);
    }

    public void showWinner(PlayerModel winner) {
        ViewUtil.runOnUiThread(getUI(), () -> {
            OkDialog dialog = new OkDialog(winner.getName() + " wins the game!");
            dialog.setCloseOnEsc(false);
            dialog.setCloseOnOutsideClick(false);
            dialog.setOkButtonLabel("Quit");
            dialog.addOkListener(event -> closeSessionAndQuit());
            add(dialog);
            dialog.addDialogCloseActionListener(e -> remove(dialog));
            dialog.open();
        });
    }

    @ClientCallable
    public void closeSession() {
        logger.info("Closing {}", this);
        controller.closePlayerSession(player);
    }

    public void closeSessionAndQuit() {
        closeSession();
        UI.getCurrent().navigate(WelcomeView.class);
    }

    private void startGame() {
        controller.startGame();
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent beforeLeaveEvent) {
        logger.info("Leaving");
    }
}
