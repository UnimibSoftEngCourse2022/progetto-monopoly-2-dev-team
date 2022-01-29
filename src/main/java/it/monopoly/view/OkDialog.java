package it.monopoly.view;


import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class OkDialog extends Dialog {
    private final Component[] components;
    private final String message;

    public OkDialog(Component... components) {
        this(null, components);
    }

    public OkDialog(String message, Component... components) {
        this.message = message;
        this.components = components;
    }

    @Override

    protected void onAttach(AttachEvent attachEvent) {
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
        Button okButton = new Button("Ok", e -> {
            close();
        });

        buttonLayout.add(okButton);
        verticalDialogLayout.add(buttonLayout);
        add(verticalDialogLayout);
    }
}
