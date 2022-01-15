import model.property.PropertyModel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PropertyRandomizerManager implements Randomizer {

    private List<PropertyModel> properties;
    private float randomnessIndex;
    private Map<PropertyModel, PropertyRandomize> randomizeMap = new ConcurrentHashMap<>();

    public PropertyRandomizerManager(List<PropertyModel> properties, float randomnessIndex) {
        this.properties = properties;
        this.randomnessIndex = randomnessIndex;
    }

    @Override
    public void randomize() {
        randomizeMap.clear();
        for (PropertyModel property : properties) {
            if (Math.random() < randomnessIndex) {
                randomizeMap.put(property, new PropertyRandomize(randomValue(),
                        randomValue(),
                        randomValue(),
                        randomValue(),
                        randomValue()));
            }
        }
    }

    public PropertyRandomize getPropertyRandomize(PropertyModel propertyModel) {
        return randomizeMap.getOrDefault(propertyModel, null);
    }

    private float randomValue() {
        float random = (float) Math.random();
        return (random - (random / 2)) * randomnessIndex;
    }
}