package it.monopoly.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;

public class BoardView extends VerticalLayout {

    private Button zoomIn;
    private Button zoomOut;
    private static final int MAX_ZOOM = 3000;
    private static final int MIN_ZOOM = 500;


    @Override
    protected void onAttach(AttachEvent attachEvent) {
        IFrame boardFrame = new IFrame("board.html");
        boardFrame.setId("board-frame");
        boardFrame.setSizeFull();
        boardFrame.getStyle().set("border", "none");

        HorizontalLayout header = new HorizontalLayout();
        header.setJustifyContentMode(JustifyContentMode.END);
        zoomIn = new Button("Zoom In", new Icon(VaadinIcon.SEARCH_PLUS));
        zoomIn.getStyle().set("margin", "0");
        zoomIn.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> zoomFrame(100));
        zoomOut = new Button("Zoom Out", new Icon(VaadinIcon.SEARCH_MINUS));
        zoomOut.getStyle().set("margin", "0");
        zoomOut.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> zoomFrame(-100));
        header.add(zoomIn, zoomOut);

        add(header, boardFrame);
        setSizeFull();
    }

    private void zoomFrame(int value) {
        UI.getCurrent()
                .getPage()
                .executeJs("return document.getElementById(\"board-frame\").contentWindow.document.getElementById(\"monopoly-board\").style.width")
                .then(String.class, (SerializableConsumer<String>) string -> {
                    int width = Integer.parseInt(string.replace("px", ""));
                    width += value;

                    applyWidth(width);

                    zoomIn.setEnabled(width < MAX_ZOOM);
                    zoomOut.setEnabled(width > MIN_ZOOM);
                });
    }

    private void applyWidth(int width) {
        UI.getCurrent()
                .getPage()
                .executeJs("return document.getElementById(\"board-frame\").contentWindow.document.getElementById(\"monopoly-board\").style.width = \"" + width + "px\"");
    }
}
