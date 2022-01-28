package it.monopoly.view;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import it.monopoly.controller.Controller;
import it.monopoly.manager.loyaltyprogram.LoyaltyProgram;
import it.monopoly.model.Configuration;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class ConfigurationView extends VerticalLayout {
    private static List<Configuration> configurations;
    private IntegerField playerNumberField;
    private IntegerField initialFundsField;
    private IntegerField randomnessIndexField;
    private IntegerField taxChangeRateField;
    private IntegerField propertiesChangeRateField;
    private Select<LoyaltyProgram.Type> loyaltySelect;
    private Select<ConfigurationView.Preset> presetSelect;

    private enum Preset {
        DEFAULT,
        EASY,
        MEDIUM,
        HARD
    }

    static {
        ObjectMapper jacksonMapper = new ObjectMapper();
        URL jsonURL = Controller.class.getClassLoader().getResource("configurations.json");
        if (jsonURL != null) {
            try {
                configurations = jacksonMapper.readValue(new File(jsonURL.toURI()), new TypeReference<>() {
                });
            } catch (IOException | URISyntaxException ignored) {
                configurations = Collections.emptyList();
            }
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        FormLayout configLayout = new FormLayout();
        configLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE)
        );

        setMargin(false);
        setPadding(false);

        presetSelect = new Select<>();
        presetSelect.setLabel("Special Configuration");
        presetSelect.setItems(Preset.values());
        presetSelect.setValue(Preset.DEFAULT);
        presetSelect.setItemLabelGenerator(
                (ItemLabelGenerator<Preset>)
                        preset -> preset != null ? StringUtils.capitalize(preset.name().toLowerCase()) : ""
        );

        playerNumberField = new IntegerField();
        playerNumberField.setHasControls(true);
        playerNumberField.setValue(6);
        playerNumberField.setMax(10);
        playerNumberField.setMin(2);
        configLayout.addFormItem(playerNumberField, "Max player number");

        initialFundsField = new IntegerField();
        initialFundsField.setHasControls(true);
        initialFundsField.setStep(100);
        initialFundsField.setValue(1000);
        initialFundsField.setMin(500);
        initialFundsField.setMax(5000);
        configLayout.addFormItem(initialFundsField, "Initial funds");

        randomnessIndexField = new IntegerField();
        randomnessIndexField.setHasControls(true);
        randomnessIndexField.setStep(10);
        randomnessIndexField.setValue(20);
        Div suffixComponent = new Div();
        suffixComponent.setText("%");
        randomnessIndexField.setSuffixComponent(suffixComponent);
        randomnessIndexField.setMin(0);
        configLayout.addFormItem(randomnessIndexField, "Randomness index");

        taxChangeRateField = new IntegerField();
        taxChangeRateField.setHasControls(true);
        taxChangeRateField.setStep(10);
        taxChangeRateField.setValue(20);
        Div suffixTax = new Div();
        suffixTax.setText("%");
        taxChangeRateField.setSuffixComponent(suffixTax);
        taxChangeRateField.setMin(0);
        taxChangeRateField.setMax(100);
        configLayout.addFormItem(taxChangeRateField, "Tax change rate");

        propertiesChangeRateField = new IntegerField();
        propertiesChangeRateField.setHasControls(true);
        propertiesChangeRateField.setStep(10);
        propertiesChangeRateField.setValue(20);
        Div suffixPropertiesRate = new Div();
        suffixPropertiesRate.setText("%");
        propertiesChangeRateField.setSuffixComponent(suffixPropertiesRate);
        propertiesChangeRateField.setMin(0);
        propertiesChangeRateField.setMax(100);
        configLayout.addFormItem(propertiesChangeRateField, "Properties values change rate");

        loyaltySelect = new Select<>();
        loyaltySelect.setEmptySelectionAllowed(true);
        loyaltySelect.setEmptySelectionCaption("None");
        loyaltySelect.setItems(LoyaltyProgram.Type.values());
        loyaltySelect.setItemLabelGenerator(
                (ItemLabelGenerator<LoyaltyProgram.Type>)
                        type -> type != null ? StringUtils.capitalize(type.name().toLowerCase()) : "None"
        );
        configLayout.addFormItem(loyaltySelect, "Loyalty program type");

        presetSelect.addValueChangeListener(
                event -> {
                    Preset value = event.getValue();
                    if (value == null) {
                        configLayout.setVisible(false);
                        return;
                    }
                    configLayout.setVisible(true);
                    applyConfigurationToView(configurations.get(value.ordinal()));
                }
        );

        add(presetSelect, configLayout);
    }

    private void applyConfigurationToView(Configuration selectedConfiguration) {
        playerNumberField.setValue(selectedConfiguration.getPlayerNumber());
        initialFundsField.setValue(selectedConfiguration.getPlayerFund());
        randomnessIndexField.setValue((int) (selectedConfiguration.getRandomnessIndex() * 100));
        taxChangeRateField.setValue((int) (selectedConfiguration.getTaxRate() * 100));
        propertiesChangeRateField.setValue((int) (selectedConfiguration.getPriceChangeRate() * 100));
        loyaltySelect.setValue(selectedConfiguration.getLoyaltyType());
    }

    public Configuration buildConfigurationFromView() {
        if (presetSelect.getValue() == null) {
            return null;
        }
        return new Configuration(
                playerNumberField.getValue(),
                initialFundsField.getValue(),
                randomnessIndexField.getValue() / 100f,
                taxChangeRateField.getValue() / 100f,
                propertiesChangeRateField.getValue() / 100f,
                loyaltySelect.getValue()
        );
    }
}
