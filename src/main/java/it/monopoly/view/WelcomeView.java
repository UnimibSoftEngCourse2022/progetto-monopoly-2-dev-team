package it.monopoly.view;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import it.monopoly.controller.Controller;
import it.monopoly.controller.RouteController;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
public class WelcomeView extends VerticalLayout {
    private final RouteController routeController;
    private ConfigurationView configurationView;
    private String noNameId = null;
    public static final String PLAYER_NAME = "PLAYER_NAME";
    public static final String NO_NAME_ID = "NO_NAME_ID";
    public static final String ERROR = "ERROR";
    private VerticalLayout createGameLayout;
    private TextField nameField;
    private VerticalLayout participateGameLayout;
    private Notification notification;

    public WelcomeView(@Autowired RouteController routeController) {
        this.routeController = routeController;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        Tab createGameTab = new Tab("Create Game");
        Tab participateGameTab = new Tab("Participate Game");

        Tabs tabs = new Tabs(createGameTab, participateGameTab);
        tabs.setSelectedTab(createGameTab);

        nameField = new TextField("Player name");
        nameField.setPlaceholder("Name");
        nameField.setRequired(true);
        nameField.setRequiredIndicatorVisible(true);

        Object playerNameObject = ComponentUtil.getData(attachEvent.getUI(), PLAYER_NAME);
        if (playerNameObject != null) {
            nameField.setValue(String.valueOf(playerNameObject));
        }

        createGameLayout = new VerticalLayout();
        createGameLayout.setPadding(false);
        createGameLayout.setMargin(false);

        participateGameLayout = new VerticalLayout();
        participateGameLayout.setPadding(false);
        participateGameLayout.setMargin(false);

        Object idObject = ComponentUtil.getData(attachEvent.getUI(), NO_NAME_ID);
        if (idObject != null) {
            noNameId = String.valueOf(idObject);
        }

        createContent();

        tabs.addSelectedChangeListener(selectedChangeEvent -> {
            Tab selectedTab = selectedChangeEvent.getSelectedTab();
            if (selectedTab != null && selectedTab.equals(createGameTab)) {
                showCreateContent();
            } else {
                showParticipateContent();
            }
        });

        if (noNameId == null) {
            tabs.setSelectedTab(createGameTab);
            showCreateContent();
        } else {
            tabs.setSelectedTab(participateGameTab);
            showParticipateContent();
        }

        add(tabs, nameField, createGameLayout, participateGameLayout);

        Object errorText = ComponentUtil.getData(attachEvent.getUI(), ERROR);
        if (errorText != null) {
            showNotification(String.valueOf(errorText));
            ComponentUtil.setData(attachEvent.getUI(), ERROR, null);
        }
    }

    private void showCreateContent() {
        participateGameLayout.setVisible(false);
        createGameLayout.setVisible(true);
    }

    private void showParticipateContent() {
        createGameLayout.setVisible(false);
        participateGameLayout.setVisible(true);
    }

    private void createContent() {
        configurationView = new ConfigurationView();
        Button createGameButton = new Button("Create Game", event -> createGame());
        createGameButton.setEnabled(false);
        nameField.addValueChangeListener(
                event -> createGameButton.setEnabled(event.getValue() != null && !event.getValue().isEmpty())
        );

        createGameLayout.add(configurationView, createGameButton);

        Button participateGameButton = new Button("Participate Game");
        participateGameButton.setEnabled(false);
        TextField gameIdField = new TextField("Game Id");
        gameIdField.setRequired(true);
        gameIdField.setRequiredIndicatorVisible(true);
        gameIdField.setPlaceholder("16 characters code");
        gameIdField.addKeyDownListener(
                Key.ENTER,
                keyDownEvent -> participateGameButton.click()
        );
        if (noNameId != null) {
            gameIdField.setValue(noNameId);
        }

        participateGameButton.addClickListener(event -> participateGame(gameIdField.getValue()));

        participateGameLayout.add(gameIdField, participateGameButton);

        HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>>
                changeListener =
                event -> participateGameButton.setEnabled(
                        nameField.getValue() != null &&
                                !nameField.getValue().isEmpty() &&
                                gameIdField.getValue() != null &&
                                !gameIdField.getValue().isEmpty()
                );
        nameField.addValueChangeListener(changeListener);
        gameIdField.addValueChangeListener(changeListener);
    }

    private void createGame() {
        String route = routeController.createGame(configurationView.buildConfigurationFromView());
        setPlayerNameData();
        navigate(route);
    }

    private void participateGame(String id) {
        if (id == null) {
            return;
        }
        Controller controller = routeController.getController(id);
        if (controller != null) {
            if (controller.maxPlayerReached()) {
                showNotification("Max player number reached");
                return;
            }
            setPlayerNameData();
            navigate(id);
        } else {
            showNotification("Game not found");
        }
    }

    private void showNotification(String text) {
        if (notification == null) {
            notification = new Notification();
            add(notification);
        } else {
            notification.close();
        }
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.setDuration(2500);
        notification.setText(text);
        notification.open();
    }

    private void setPlayerNameData() {
        ComponentUtil.setData(UI.getCurrent(), PLAYER_NAME, nameField.getValue());
    }

    private void navigate(String id) {
        UI.getCurrent().navigate(id);
    }
}
