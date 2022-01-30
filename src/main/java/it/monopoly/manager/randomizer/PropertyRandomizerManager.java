package it.monopoly.manager.randomizer;

import it.monopoly.model.PropertyRandomizeModel;
import it.monopoly.model.property.PropertyModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class PropertyRandomizerManager implements Randomizer {

    private final List<PropertyModel> properties;
    private final float randomnessIndex;
    private final Map<PropertyModel, PropertyRandomizeModel> randomizeMap = new ConcurrentHashMap<>();
    private final PropertyRandomizeModel defaultRandomizeModel;
    private final Random random = new Random();
    Logger logger = LogManager.getLogger(getClass());

    public PropertyRandomizerManager(List<PropertyModel> properties, float randomnessIndex) {
        this.properties = properties;
        this.randomnessIndex = randomnessIndex;
        this.defaultRandomizeModel = new PropertyRandomizeModel(0,
                0,
                0,
                0,
                0);
    }

    @Override
    public void randomize() {
        logger.info("Randomizing some properties values");
        randomizeMap.clear();
        for (PropertyModel property : properties) {
            if (random.nextFloat() < randomnessIndex) {
                PropertyRandomizeModel randomizeModel = new PropertyRandomizeModel(randomValue(),
                        randomValue(),
                        randomValue(),
                        randomValue(),
                        randomValue());
                logger.info("Randomizing {} -> {}", property.getName(), randomizeModel);
                randomizeMap.put(property, randomizeModel);
            }
        }
    }

    public PropertyRandomizeModel getPropertyRandomize(PropertyModel propertyModel) {
        return randomizeMap.getOrDefault(propertyModel, defaultRandomizeModel);
    }

    private float randomValue() {
        float value = random.nextFloat();
        return (value - (value / 2));
    }

    public Map<PropertyModel, PropertyRandomizeModel> getRandomizeMap() {
        return randomizeMap;
    }
}