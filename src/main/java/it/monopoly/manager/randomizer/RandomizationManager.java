package it.monopoly.manager.randomizer;

import it.monopoly.model.Configuration;
import it.monopoly.model.property.PropertyModel;

import java.util.List;
import java.util.Random;

public class RandomizationManager implements Randomizer {
    private final PropertyRandomizerManager propertyRandomizerManager;
    private final TaxRandomizerManager taxRandomizerManager;
    private final Random random = new Random();
    private float priceChangeRate = .25f;
    private float taxChangeRate = .25f;

    public RandomizationManager(List<PropertyModel> properties, Configuration configuration) {
        float randomnessIndex;
        if (configuration != null) {
            randomnessIndex = configuration.getRandomnessIndex();
            priceChangeRate = configuration.getPriceChangeRate();
            taxChangeRate = configuration.getTaxRate();
        } else {
            randomnessIndex = .25f;
        }
        this.propertyRandomizerManager = new PropertyRandomizerManager(properties, randomnessIndex);
        this.taxRandomizerManager = new TaxRandomizerManager(randomnessIndex);
    }

    @Override
    public void randomize() {
        if (random.nextFloat() < priceChangeRate) {
            propertyRandomizerManager.randomize();
        }
        if (random.nextFloat() < taxChangeRate) {
            taxRandomizerManager.randomize();
        }
    }

    public PropertyRandomizerManager getPropertyRandomizerManager() {
        return propertyRandomizerManager;
    }

    public TaxRandomizerManager getTaxRandomizerManager() {
        return taxRandomizerManager;
    }
}
