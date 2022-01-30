package it.monopoly.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.server.Command;
import it.monopoly.model.property.ReadablePropertyModel;

import java.util.List;

public class PropertyListView extends Div {

    private final Grid<ReadablePropertyModel> grid;
    private final SelectionListener<Grid<ReadablePropertyModel>, ReadablePropertyModel> selectionListener;

    public PropertyListView() {
        this(null);
    }

    public PropertyListView(SelectionListener<Grid<ReadablePropertyModel>, ReadablePropertyModel> selectionListener) {
        this.selectionListener = selectionListener;
        grid = new Grid<>(ReadablePropertyModel.class, false);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (selectionListener != null) {
            grid.addSelectionListener(selectionListener);
        }
        grid.addColumn(ReadablePropertyModel::getName).setHeader("Street name").setAutoWidth(true).setResizable(true);
        grid.addColumn(ReadablePropertyModel::getHouseNumber).setHeader("House number").setAutoWidth(true).setResizable(true);
        grid.addColumn(ReadablePropertyModel::getHotelNumber).setHeader("Hotel number").setAutoWidth(true).setResizable(true);
        grid.addColumn(ReadablePropertyModel::getRent).setHeader("Rent").setAutoWidth(true).setResizable(true);
        grid.addColumn(ReadablePropertyModel::getCategory).setHeader("Category").setAutoWidth(true).setResizable(true);

        grid.setSizeFull();

        setSizeFull();
        add(grid);
    }

    public void setProperties(List<ReadablePropertyModel> properties) {
        getUI().ifPresent(ui -> ui.access((Command) () -> grid.setItems(properties)));
    }
}
