package it.monopoly.manager.randomizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TaxRandomizerManager implements Randomizer {
    Logger logger = LogManager.getLogger(getClass());
    private float taxesPercentage = 0F;
    private float randomnessIndex;

    public TaxRandomizerManager(float randomnessIndex) {
        this.randomnessIndex = randomnessIndex;
    }

    @Override
    public void randomize() {
        taxesPercentage = randomValue();
        logger.info("Randomizing taxes -> " + taxesPercentage);
    }

    private float randomValue() {
        float random = (float) Math.random();
        return ((random * 12) - 2) * randomnessIndex;
    }

    public float getTaxesPercentage() {
        return taxesPercentage;
    }
}
