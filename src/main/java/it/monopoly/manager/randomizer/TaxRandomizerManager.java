package it.monopoly.manager.randomizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class TaxRandomizerManager implements Randomizer {
    private final float randomnessIndex;
    private final Random random = new Random();
    Logger logger = LogManager.getLogger(getClass());
    private float taxesPercentage = 0F;

    public TaxRandomizerManager(float randomnessIndex) {
        this.randomnessIndex = randomnessIndex;
    }

    @Override
    public void randomize() {
        taxesPercentage = randomValue();
        logger.info("Randomizing taxes -> {}", taxesPercentage);
    }

    private float randomValue() {
        float value = random.nextFloat();
        return ((value * 12) - 2) * randomnessIndex;
    }

    public float getTaxesPercentage() {
        return taxesPercentage;
    }
}
