package it.monopoly.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import it.monopoly.controller.event.callback.FirstOrSecondCallback;
import it.monopoly.controller.event.callback.FirstSecondChoice;

public class FirstSecondChoiceDialog extends Dialog {

    private final HorizontalLayout buttonLayout;
    private final VerticalLayout verticalDialogLayout;
    private final Button firstButton;
    private final Button secondButton;

    public FirstSecondChoiceDialog(FirstOrSecondCallback callback) {
        verticalDialogLayout = new VerticalLayout();
        buttonLayout = new HorizontalLayout();

        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        firstButton = new Button("Yes", e -> {
            callback.choose(FirstSecondChoice.FIRST);
            close();
        });
        secondButton = new Button("No", e -> {
            callback.choose(FirstSecondChoice.SECOND);
            close();
        });
        buttonLayout.add(firstButton, secondButton);

        verticalDialogLayout.add(buttonLayout);
        add(verticalDialogLayout);
    }

    public void setFirstChoice(String first) {
        firstButton.setText(first);
    }

    public void setSecondChoice(String second) {
        secondButton.setText(second);
    }

    public void setText(String message) {
        verticalDialogLayout.removeAll();
        verticalDialogLayout.add(message);
        verticalDialogLayout.add(buttonLayout);
    }
}
