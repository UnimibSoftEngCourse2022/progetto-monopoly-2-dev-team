package it.monopoly.view;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import it.monopoly.controller.Controller;
import it.monopoly.controller.RouteController;
import it.monopoly.manager.loyaltyprogram.LoyaltyProgram;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
public class WelcomeView extends VerticalLayout {
    private final RouteController routeController;
    private static final String NONE = "None";
    private static final String CUSTOM = "Custom";
    private static final String EASY = "Easy";
    private static final String MEDIUM = "Medium";
    private static final String HARD = "Hard";


    public WelcomeView(@Autowired RouteController routeController) {
        this.routeController = routeController;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        TextField nameField = new TextField("Player name");

        Button createGameButton = new Button("Create Game", e -> createGame());

        VerticalLayout configLayout = new VerticalLayout();
        configLayout.setVisible(false);

        IntegerField playerNumberField = new IntegerField("Max player number");
        playerNumberField.setHasControls(true);
        playerNumberField.setValue(6);
        playerNumberField.setMax(10);
        playerNumberField.setMin(2);

        IntegerField initialFundsField = new IntegerField("Initial funds");
        initialFundsField.setHasControls(true);
        initialFundsField.setStep(100);
        initialFundsField.setValue(1000);
        initialFundsField.setMin(500);
        initialFundsField.setMax(5000);

        IntegerField randomnessIndexField = new IntegerField("Randomness index");
        randomnessIndexField.setHasControls(true);
        randomnessIndexField.setStep(10);
        randomnessIndexField.setValue(20);
        Div suffixComponent = new Div();
        suffixComponent.setText("%");
        randomnessIndexField.setSuffixComponent(suffixComponent);
        randomnessIndexField.setMin(0);

        IntegerField taxChangeRateField = new IntegerField("Tax change rate");
        taxChangeRateField.setHasControls(true);
        taxChangeRateField.setStep(10);
        taxChangeRateField.setValue(20);
        Div suffixTax = new Div();
        suffixTax.setText("%");
        taxChangeRateField.setSuffixComponent(suffixTax);
        taxChangeRateField.setMin(0);
        taxChangeRateField.setMax(100);

        IntegerField propertiesChangeRateField = new IntegerField("Properties values change rate");
        propertiesChangeRateField.setHasControls(true);
        propertiesChangeRateField.setStep(10);
        propertiesChangeRateField.setValue(20);
        Div suffixPropertiesRate = new Div();
        suffixPropertiesRate.setText("%");
        propertiesChangeRateField.setSuffixComponent(suffixPropertiesRate);
        propertiesChangeRateField.setMin(0);
        propertiesChangeRateField.setMax(100);

        Select<LoyaltyProgram.Type> loyaltySelect = new Select<>();
        loyaltySelect.setEmptySelectionAllowed(true);
        loyaltySelect.setLabel("Loyalty program type");
        loyaltySelect.setItems(LoyaltyProgram.Type.values());
        loyaltySelect.setItemLabelGenerator(
                (ItemLabelGenerator<LoyaltyProgram.Type>)
                        type -> type != null ? StringUtils.capitalize(type.name().toLowerCase()) : "None"
        );

        configLayout.add(
                playerNumberField,
                initialFundsField,
                randomnessIndexField,
                taxChangeRateField,
                propertiesChangeRateField,
                loyaltySelect
        );

        Select<String> select = new Select<>();
        select.setLabel("Special Configuration");
        select.setItems(NONE, CUSTOM, EASY, MEDIUM, HARD);
        select.setValue(NONE);
        select.addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<Select<String>, String>>)
                event -> {
                    if (NONE.equals(event.getValue())) {
                        configLayout.setVisible(false);
                        return;
                    }
                    configLayout.setVisible(true);
                });

        add(nameField, select, configLayout, createGameButton);
    }

    private void createGame() {
        String id = RandomStringUtils.randomAlphanumeric(16).toLowerCase();
        routeController.createGame(null, id);
        navigate(id);
    }

    private void participateGame(String id) {
        Controller controller = routeController.getController(id);
        if (controller != null) {
            navigate(id);
        } else {
            Notification notification = new Notification("Game does not exist");
            add(notification);
        }
    }

    private void navigate(String id) {
        UI.getCurrent().navigate(id);
    }
}
