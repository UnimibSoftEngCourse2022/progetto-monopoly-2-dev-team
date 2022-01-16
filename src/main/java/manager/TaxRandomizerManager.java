package manager;

public class TaxRandomizerManager implements Randomizer {

    private float taxesPercentage = 0F;
    private float randomnessIndex;

    public TaxRandomizerManager(float randomnessIndex) {
        this.randomnessIndex = randomnessIndex;
    }

    @Override
    public void randomize() {
        if (Math.random() < randomnessIndex) {
            taxesPercentage = randomValue();
        }
    }

    private float randomValue() {
        float random = (float) Math.random();
        return ((random * 12) - 2) * randomnessIndex;
    }

    public float getTaxesPercentage() {
        return taxesPercentage;
    }
}
