import model.property.PropertyModel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PropertyRandomizer implements Randomizer {

    private float percentagePrice;
    private List<PropertyModel> properties;
    private final Map<PropertyModel, PropertyRandomization> randomizationMap = new ConcurrentHashMap<>();

    @Override
    public void randomize() {
        setPercentagePrice((float) Math.random());
    }

    @Override
    public void randomize(int value) {
        setPercentagePrice((float) Math.random() * value);
    }

    public PropertyRandomization getRandomization(PropertyModel property) {
        return randomizationMap.get(property);
    }

    public float getPercentagePrice() {
        return percentagePrice;
    }

    public void setPercentagePrice(float percentagePrice) {
        this.percentagePrice = percentagePrice;
    }
}
