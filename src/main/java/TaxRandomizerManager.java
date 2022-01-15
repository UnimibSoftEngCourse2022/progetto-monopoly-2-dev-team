public class TaxRandomizerManager implements Randomizer{

    private float taxesPercentage;
    private float randomnessIndex;

    public TaxRandomizerManager(float taxesPercentage) {
        this.taxesPercentage = randomValue();
    }

    @Override
    public void randomize() {
        taxesPercentage = randomValue();
    }

    private float randomValue() {
        float random = (float) Math.random();
        return ((random * 12) - 2) * randomnessIndex;
    }

    public float getTaxesPercentage() {
        return taxesPercentage;
    }
}
