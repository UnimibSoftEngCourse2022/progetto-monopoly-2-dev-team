package it.monopoly.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import it.monopoly.controller.event.callback.BuyOrAuctionCallback;
import it.monopoly.model.property.ReadablePropertyModel;

public class BuyOrAuctionView extends Dialog {

    private final ReadablePropertyModel property;
    private final BuyOrAuctionCallback callback;

    public BuyOrAuctionView(ReadablePropertyModel property, BuyOrAuctionCallback callback) {
        this.property = property;
        this.callback = callback;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        VerticalLayout verticalDialogLayout = new VerticalLayout();
        verticalDialogLayout.add(property.toString());

        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button yesButton = new Button("Buy", e -> {
            callback.buy();
            close();
        });
        Button noButton = new Button("Start Auction", e -> {
            callback.startAuction();
            close();
        });

        buttonLayout.add(yesButton, noButton);
        verticalDialogLayout.add(buttonLayout);
        add(verticalDialogLayout);
    }
}
