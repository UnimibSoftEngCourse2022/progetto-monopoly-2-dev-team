package it.monopoly.view;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.server.Command;
import it.monopoly.manager.AuctionManager;
import it.monopoly.model.AuctionModel;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.player.ReadablePlayerModel;

import java.util.Collection;

public class AuctionView extends VerticalLayout implements Observer<Collection<AuctionModel>> {

    private final PlayerModel player;
    private final AuctionManager manager;
    private final ReadablePlayerModel readablePlayer;
    private Grid<AuctionModel> grid;
    private NumberField numberField;
    private Button offerButton;

    public AuctionView(PlayerModel player, ReadablePlayerModel readablePlayer, AuctionManager manager) {
        this.player = player;
        this.readablePlayer = readablePlayer;
        this.manager = manager;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        manager.register(this);

        grid = new Grid<>();
        grid.addColumn(AuctionModel::getPlayer).setHeader("Player");
        grid.addColumn(AuctionModel::getAmount).setHeader("Amount");
        grid.setWidthFull();
        grid.setHeight(70, Unit.PERCENTAGE);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidthFull();
        horizontalLayout.setHeight(30, Unit.PERCENTAGE);
        horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);

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
                        makeOffer(numberField.getValue().intValue());
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

        add(grid, horizontalLayout);
        setHeight(50, Unit.PERCENTAGE);
    }

    private void makeOffer(int amount) {
        if (amount <= readablePlayer.getFunds()) {
            manager.makeOffer(player, amount);
        }
    }

    public void setAuctionModels(Collection<AuctionModel> auctionModels) {
        getUI().ifPresent(ui -> ui.access((Command) () -> grid.setItems(auctionModels)));
    }

    @Override
    public void notify(Collection<AuctionModel> obj) {
        setAuctionModels(obj);
        if (numberField.getMax() > manager.getBestOffer()) {
            numberField.setMin(manager.getBestOffer());
        } else {
            offerButton.setEnabled(false);
        }
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        if (manager != null) {
            manager.deregister(this);
        }
    }
}
