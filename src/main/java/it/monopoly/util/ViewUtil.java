package it.monopoly.util;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.Command;

import java.util.Optional;

public class ViewUtil {
    public static void runOnUiThread(Optional<UI> optionalUI, Runnable runnable) {
        optionalUI.ifPresent(ui -> ui.access((Command) runnable::run));
    }
}
