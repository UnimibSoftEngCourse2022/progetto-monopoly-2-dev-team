package it.monopoly.manager.randomizer;

import it.monopoly.model.PropertyRandomizeModel;
import it.monopoly.model.property.PropertyModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PropertyRandomizerManager implements Randomizer {

    Logger logger = LogManager.getLogger(getClass());
    private List<PropertyModel> properties;
    private float randomnessIndex;
    private Map<PropertyModel, PropertyRandomizeModel> randomizeMap = new ConcurrentHashMap<>();

    public PropertyRandomizerManager(List<PropertyModel> properties, float randomnessIndex) {
        this.properties = properties;
        this.randomnessIndex = randomnessIndex;
    }

    @Override
    public void randomize() {
        logger.info("Randomizing some properties values");
        randomizeMap.clear();
        for (PropertyModel property : properties) {
            if (Math.random() < randomnessIndex) {
                PropertyRandomizeModel randomizeModel = new PropertyRandomizeModel(randomValue(),
                        randomValue(),
                        randomValue(),
                        randomValue(),
                        randomValue());
                logger.info("Randomizing " + property.getName() + " -> " + randomizeModel);
                randomizeMap.put(property, randomizeModel);
            }
        }
    }

    public PropertyRandomizeModel getPropertyRandomize(PropertyModel propertyModel) {
        return randomizeMap.getOrDefault(propertyModel, null);
    }

    private float randomValue() {
        float random = (float) Math.random();
        return (random - (random / 2));// * randomnessIndex;
    }

    public Map<PropertyModel, PropertyRandomizeModel> getRandomizeMap() {
        return randomizeMap;
    }
}