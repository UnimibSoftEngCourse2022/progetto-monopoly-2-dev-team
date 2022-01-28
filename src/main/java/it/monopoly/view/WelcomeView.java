package it.monopoly.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import it.monopoly.controller.Controller;
import it.monopoly.controller.RouteController;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
public class WelcomeView extends VerticalLayout {
    private final RouteController routeController;
    private ConfigurationView configurationView;
    public static final String PLAYER_NAME = "PLAYER_NAME";
    private VerticalLayout content;
    private TextField nameField;

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

        content = new VerticalLayout();
        content.setPadding(false);
        content.setMargin(false);

        tabs.addSelectedChangeListener(selectedChangeEvent -> {
            Tab selectedTab = selectedChangeEvent.getSelectedTab();
            if (selectedTab != null && selectedTab.equals(createGameTab)) {
                setCreateContent();
            } else {
                setParticipateContent();
            }
        });
        setCreateContent();

        add(tabs, nameField, content);
    }

    private void setCreateContent() {
        if (content == null) {
            return;
        }
        content.removeAll();
        configurationView = new ConfigurationView();
        Button createGameButton = new Button("Create Game", event -> createGame());

        content.add(configurationView, createGameButton);
    }

    private void setParticipateContent() {
        if (content == null) {
            return;
        }
        content.removeAll();
        Button participateGameButton = new Button("Create Game");

        TextField gameIdField = new TextField("Game Id");
        gameIdField.setPlaceholder("16 characters code");
        gameIdField.addKeyDownListener(
                Key.ENTER,
                keyDownEvent -> participateGameButton.click()
        );

        participateGameButton.addClickListener(event -> participateGame(gameIdField.getValue()));

        content.add(gameIdField, participateGameButton);
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
            setPlayerNameData();
            navigate(id);
        } else {
            Notification notification = new Notification("Game does not exist");
            notification.setPosition(Notification.Position.TOP_CENTER);
            add(notification);
            notification.setDuration(2500);
            notification.open();
        }
    }

    private void setPlayerNameData() {
        ComponentUtil.setData(UI.getCurrent(), PLAYER_NAME, nameField.getValue());
    }

    private void navigate(String id) {
        UI.getCurrent().navigate(id);
    }
}
