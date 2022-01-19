package it.monopoly.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.data.selection.SelectionListener;
import it.monopoly.model.property.PropertyModel;

import java.util.List;

public class PropertyListView extends Div {

    private final Grid<PropertyModel> grid;
    private final SelectionListener<Grid<PropertyModel>, PropertyModel> selectionListener;

    public PropertyListView(SelectionListener<Grid<PropertyModel>, PropertyModel> selectionListener) {
        this.selectionListener = selectionListener;
        grid = new Grid<>(PropertyModel.class, false);
        grid.addSelectionListener(selectionListener);
        grid.removeAllColumns();
        grid.addColumn(PropertyModel::getName).setHeader("Street name");
        grid.addColumn(PropertyModel::getPrice).setHeader("Price");
        grid.addColumn(PropertyModel::getMortgageValue).setHeader("Mortgage");
        grid.setWidthFull();
        add(grid);
    }

    public void setProperties(List<PropertyModel> properties) {
//        if(properties == null || properties.equals(this.properties)) {
//            return;
//        }
        grid.setItems(properties);
    }
}
