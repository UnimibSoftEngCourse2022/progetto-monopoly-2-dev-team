package it.monopoly.view;


import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class OkDialog extends Dialog {
    private final Button okButton;

    public OkDialog(Component... components) {
        this(null, components);
    }

    public OkDialog(String message, Component... components) {

        setCloseOnEsc(false);
        VerticalLayout verticalDialogLayout = new VerticalLayout();
        verticalDialogLayout.setSizeFull();
        if (message != null) {
            verticalDialogLayout.add(message);
        }
        if (components.length != 0) {
            verticalDialogLayout.add(components);
        }

        HorizontalLayout buttonLayout = new HorizontalLayout();
        okButton = new Button("Ok", e -> close());

        buttonLayout.add(okButton);
        verticalDialogLayout.add(buttonLayout);
        add(verticalDialogLayout);
    }

    public void addOkListener(ComponentEventListener<ClickEvent<Button>> listener) {
        okButton.addClickListener(listener);
    }

    public void setOkButtonLabel(String label) {
        okButton.setText(label);
    }
}
