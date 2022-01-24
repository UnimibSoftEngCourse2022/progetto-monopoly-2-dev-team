package it.monopoly.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.server.Command;
import it.monopoly.model.property.ReadablePropertyModel;

import java.util.List;

public class PropertyListView extends Div {

    private final Grid<ReadablePropertyModel> grid;

    public PropertyListView(SelectionListener<Grid<ReadablePropertyModel>, ReadablePropertyModel> selectionListener) {

        grid = new Grid<>(ReadablePropertyModel.class, false);
        grid.addSelectionListener(selectionListener);
        grid.addColumn(ReadablePropertyModel::getName).setHeader("Street name");
        grid.addColumn(ReadablePropertyModel::getHouseNumber).setHeader("House number");
        grid.addColumn(ReadablePropertyModel::getHotelNumber).setHeader("Hotel number");
        grid.addColumn(ReadablePropertyModel::getRent).setHeader("Rent");
        grid.addColumn(ReadablePropertyModel::getCategory).setHeader("Category");

        grid.setSizeFull();

        setSizeFull();
        add(grid);
    }

    public void setProperties(List<ReadablePropertyModel> properties) {
        getUI().ifPresent(ui -> ui.access((Command) () -> grid.setItems(properties)));
    }
}
