package it.monopoly.view;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import it.monopoly.controller.Controller;
import it.monopoly.controller.command.Command;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.player.ReadablePlayerModel;
import it.monopoly.model.property.ReadablePropertyModel;

import java.util.List;

public class PlayerListView extends VerticalLayout {

    private final Grid<ReadablePlayerModel> grid;
    private final Controller controller;
    private final HorizontalLayout buttonsLayout;
    private PlayerModel player;

    public PlayerListView(Controller controller) {
        this.controller = controller;

        grid = new Grid<>(ReadablePlayerModel.class, false);

        grid.addColumn(TemplateRenderer.<ReadablePlayerModel>of(
                "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 100 100\" style=\"width: 24px; fill: [[item.color]];\"><circle cx=\"50\" cy=\"50\" r=\"50\" style=\"position: relative;\"></circle></svg>"
        ).withProperty("color", ReadablePlayerModel::getColor)).setHeader("Color").setAutoWidth(true).setResizable(true);
        grid.addColumn(ReadablePlayerModel::getName).setHeader("Name").setAutoWidth(true).setResizable(true);
        grid.addColumn(ReadablePlayerModel::getFunds).setHeader("Funds").setAutoWidth(true).setResizable(true);
        grid.addColumn(ReadablePlayerModel::getStateName).setHeader("State");

        grid.setSizeFull();

        setMargin(false);
        setPadding(false);
        setSizeFull();

        buttonsLayout = new HorizontalLayout();

        grid.addSelectionListener(event -> {
            event.getFirstSelectedItem().ifPresentOrElse(this::updateButtons, buttonsLayout::removeAll);
        });

        add(grid, buttonsLayout);
    }

    private void updateButtons(ReadablePlayerModel selectedPlayer) {
        buttonsLayout.removeAll();
        Button showPropertiesButton = new Button("Show properties", event -> showPropertiesDialog(selectedPlayer.getModel()));
        buttonsLayout.add(showPropertiesButton);

        List<Command> commands = controller.getCommandController().generateCommand(player, selectedPlayer.getModel());
        for (Command command : commands) {
            if (command != null) {
                Button button = new Button(command.getCommandName(), event -> command.execute());
                button.setEnabled(command.isEnabled());
                buttonsLayout.add(button);
            }
        }
    }

    private void showPropertiesDialog(PlayerModel playerModel) {
        List<ReadablePropertyModel> properties = controller.getReadableProperties(playerModel);

        PropertyListView propertyListView = new PropertyListView();

        OkDialog dialog = new OkDialog(propertyListView);
        dialog.setCloseOnEsc(true);
        dialog.setOkButtonLabel("Close");
        dialog.setWidth(80, Unit.PERCENTAGE);
        dialog.setHeight(80, Unit.PERCENTAGE);
        add(dialog);
        dialog.addDialogCloseActionListener(e -> PlayerListView.this.remove(dialog));
        dialog.open();
        propertyListView.setProperties(properties);
    }

    public void setPlayers(PlayerModel player, List<ReadablePlayerModel> allPlayers) {
        this.player = player;
        getUI().ifPresent(ui -> ui.access(() -> grid.setItems(allPlayers)));
    }
}
