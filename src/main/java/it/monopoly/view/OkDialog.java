package it.monopoly.view;


import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class OkDialog extends Dialog {
    private final String message;

    public OkDialog(String message) {
        this.message = message;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        setCloseOnEsc(false);
        VerticalLayout verticalDialogLayout = new VerticalLayout();
        verticalDialogLayout.add(message);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button okButton = new Button("Ok", e -> {
            close();
        });

        buttonLayout.add(okButton);
        verticalDialogLayout.add(buttonLayout);
        add(verticalDialogLayout);
    }
}
