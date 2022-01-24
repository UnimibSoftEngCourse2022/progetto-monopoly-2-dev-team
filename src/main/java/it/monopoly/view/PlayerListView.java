package it.monopoly.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.server.Command;
import it.monopoly.model.player.ReadablePlayerModel;

import java.util.List;

public class PlayerListView extends Div {

    private final Grid<ReadablePlayerModel> grid;

    public PlayerListView() {

        grid = new Grid<>(ReadablePlayerModel.class, false);
        grid.addColumn(ReadablePlayerModel::getName).setHeader("Name");
        grid.addColumn(ReadablePlayerModel::getFunds).setHeader("Funds");
        grid.addColumn(ReadablePlayerModel::getStateName).setHeader("State");

        grid.setSizeFull();

        setSizeFull();
        add(grid);
    }

    public void setPlayers(List<ReadablePlayerModel> players) {
        getUI().ifPresent(ui -> ui.access((Command) () -> grid.setItems(players)));
    }
}
