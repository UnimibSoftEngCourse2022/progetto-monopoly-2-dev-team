package it.monopoly.view;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.selection.SelectionListener;
import it.monopoly.manager.AbstractOfferManager;
import it.monopoly.model.OfferModel;
import it.monopoly.model.enums.OfferType;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.player.ReadablePlayerModel;
import it.monopoly.util.ViewUtil;

import java.util.Collection;

public class OffersView extends VerticalLayout implements Observer<Collection<OfferModel>> {

    private final PlayerModel player;
    private final AbstractOfferManager manager;
    private final OfferType type;
    private final ReadablePlayerModel readablePlayer;
    private Grid<OfferModel> grid;
    private NumberField numberField;
    private Button offerButton;
    private HorizontalLayout horizontalLayout;

    public OffersView(PlayerModel player, ReadablePlayerModel readablePlayer, AbstractOfferManager manager) {
        this.player = player;
        this.readablePlayer = readablePlayer;
        this.manager = manager;
        this.type = manager.getType();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        manager.register(this);

        grid = new Grid<>();
        grid.addColumn(OfferModel::getPlayer).setHeader("Player");
        grid.addColumn(OfferModel::getAmount).setHeader("Amount");
        grid.setWidthFull();
        grid.setHeight(70, Unit.PERCENTAGE);

        horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidthFull();
        horizontalLayout.setHeight(30, Unit.PERCENTAGE);
        horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        if (OfferType.SELL.equals(manager.getType()) && manager.isSuperVisor(player)) {
            layoutForSupervisor();

        } else {
            layoutForOffers();
        }

        add(grid, horizontalLayout);
        setHeight(50, Unit.PERCENTAGE);
    }

    private void layoutForOffers() {
        numberField = new NumberField();
        numberField.setPlaceholder("Amount");
        numberField.setMax(readablePlayer.getFunds());
        numberField.setMin(0);
        numberField.setHeightFull();

        offerButton = new Button("Send");
        offerButton.addClickListener(
                (ComponentEventListener<ClickEvent<Button>>) buttonClickEvent
                        -> {
                    if (numberField.getValue() != null &&
                            numberField.getValue() < numberField.getMax() &&
                            numberField.getValue() > numberField.getMin()) {
                        int amount = numberField.getValue().intValue();
                        makeOffer(amount);
                        if (OfferType.SELL.equals(type)) {
                            numberField.setMin(amount);
                        }
                        numberField.clear();
                    }
                }
        );

        numberField.addKeyDownListener(
                Key.ENTER,
                (ComponentEventListener<KeyDownEvent>) keyDownEvent
                        -> offerButton.click()
        );

        horizontalLayout.add(numberField, offerButton);
    }

    private void layoutForSupervisor() {
        Button highestButton = new Button("Accept Highest");
        highestButton.addClickListener(
                (ComponentEventListener<ClickEvent<Button>>) buttonClickEvent
                        -> manager.endOffers()
        );

        Button pickButton = new Button("Pick Offer");
        pickButton.setEnabled(false);
        pickButton.addClickListener(
                (ComponentEventListener<ClickEvent<Button>>) buttonClickEvent
                        -> grid.getSelectionModel().getFirstSelectedItem().ifPresent(manager::endOffers)
        );

        grid.addSelectionListener((SelectionListener<Grid<OfferModel>, OfferModel>) selectionEvent
                -> selectionEvent.getFirstSelectedItem().ifPresentOrElse
                (
                        offerModel -> pickButton.setEnabled(true),
                        () -> pickButton.setEnabled(false)
                )
        );

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(
                (ComponentEventListener<ClickEvent<Button>>) buttonClickEvent
                        -> manager.endOffers(null)
        );

        horizontalLayout.add(highestButton, pickButton, cancelButton);
    }

    private void makeOffer(int amount) {
        if (amount <= readablePlayer.getFunds()) {
            manager.makeOffer(player, amount);
        }
    }

    public void setOfferModels(Collection<OfferModel> offerModels) {
        ViewUtil.runOnUiThread(getUI(), () -> grid.setItems(offerModels));
    }

    @Override
    public void notify(Collection<OfferModel> obj) {
        ViewUtil.runOnUiThread(getUI(), () -> {
            setOfferModels(obj);
            if (numberField != null && offerButton != null && !OfferType.SELL.equals(type)) {
                if (numberField.getMax() > manager.getBestOffer()) {
                    numberField.setMin(manager.getBestOffer());
                } else {
                    offerButton.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        if (manager != null) {
            manager.deregister(this);
        }
    }
}
