public class TaxRandomizer implements Randomizer {

    private float percentageTaxes;

    @Override
    public void randomize() {
        //percentageTaxes = (float) Math.random();
        setPercentageTaxes((float) Math.random());
    }

    @Override
    public void randomize(int value) {
        setPercentageTaxes((float) Math.random() * value);
    }


    public float getPercentageTaxes() {
        return percentageTaxes;
    }

    public void setPercentageTaxes(float percentageTaxes) {
        this.percentageTaxes = percentageTaxes;
    }
}
