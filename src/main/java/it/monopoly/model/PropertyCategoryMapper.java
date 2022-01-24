package it.monopoly.model;

import it.monopoly.model.property.PropertyCategory;
import it.monopoly.model.property.PropertyModel;

import java.util.List;

public interface PropertyCategoryMapper {
    List<PropertyModel> getCategoryProperties(PropertyCategory category);
}
