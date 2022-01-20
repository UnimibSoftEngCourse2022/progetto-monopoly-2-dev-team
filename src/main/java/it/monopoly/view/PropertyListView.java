package it.monopoly.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.server.Command;
import it.monopoly.model.property.PropertyModel;

import java.util.List;

public class PropertyListView extends Div {

    private final Grid<PropertyModel> grid;
    private final SelectionListener<Grid<PropertyModel>, PropertyModel> selectionListener;

    public PropertyListView(SelectionListener<Grid<PropertyModel>, PropertyModel> selectionListener) {
        this.selectionListener = selectionListener;

        grid = new Grid<>(PropertyModel.class, false);
        grid.addSelectionListener(selectionListener);
        grid.addColumn(PropertyModel::getName).setHeader("Street name");
        grid.addColumn(PropertyModel::getHouseNumber).setHeader("House number");
        grid.addColumn(PropertyModel::getHotelNumber).setHeader("Hotel number");
        grid.addColumn(PropertyModel::getCategory).setHeader("Category");

        grid.setSizeFull();

        setSizeFull();
        add(grid);
    }

    public void setProperties(List<PropertyModel> properties) {
        getUI().ifPresent(ui -> ui.access((Command) () -> grid.setItems(properties)));
    }
}
