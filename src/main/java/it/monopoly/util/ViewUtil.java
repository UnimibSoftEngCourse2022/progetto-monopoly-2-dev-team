package it.monopoly.util;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.Command;

import java.util.Optional;

public class ViewUtil {
    private ViewUtil() {
    }

    public static void runOnUiThread(Optional<UI> optionalUI, Runnable runnable) {
        optionalUI.ifPresent(ui -> {
            if (!ui.isClosing()) {
                ui.access((Command) runnable::run);
            }
        });
    }
}
