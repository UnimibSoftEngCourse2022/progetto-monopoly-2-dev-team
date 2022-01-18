package it.monopoly.manager;

import it.monopoly.model.PropertyRandomizeModel;
import it.monopoly.model.property.PropertyModel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PropertyRandomizerManager implements Randomizer {

    private List<PropertyModel> properties;
    private float randomnessIndex;
    private Map<PropertyModel, PropertyRandomizeModel> randomizeMap = new ConcurrentHashMap<>();

    public PropertyRandomizerManager(List<PropertyModel> properties, float randomnessIndex) {
        this.properties = properties;
        this.randomnessIndex = randomnessIndex;
    }

    @Override
    public void randomize() {
        randomizeMap.clear();
        for (PropertyModel property : properties) {
            if (Math.random() < randomnessIndex) {
                randomizeMap.put(property, new PropertyRandomizeModel(randomValue(),
                        randomValue(),
                        randomValue(),
                        randomValue(),
                        randomValue()));
            }
        }
    }

    public PropertyRandomizeModel getPropertyRandomize(PropertyModel propertyModel) {
        return randomizeMap.getOrDefault(propertyModel, null);
    }

    private float randomValue() {
        float random = (float) Math.random();
        return (random - (random / 2)) * randomnessIndex;
    }

    public Map<PropertyModel, PropertyRandomizeModel> getRandomizeMap() {
        return randomizeMap;
    }
}