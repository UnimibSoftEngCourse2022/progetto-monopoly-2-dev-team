package model;

import model.property.PropertyCategory;
import model.property.PropertyModel;

import java.util.List;

public interface PropertyCategoryMapper {
    List<PropertyModel> getCategoryProperties(PropertyCategory category);
}
