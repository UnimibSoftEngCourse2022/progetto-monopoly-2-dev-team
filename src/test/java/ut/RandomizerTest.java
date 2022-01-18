package ut;

import it.monopoly.manager.PropertyRandomizerManager;
import it.monopoly.manager.TaxRandomizerManager;
import it.monopoly.model.PropertyRandomizeModel;
import it.monopoly.model.property.PropertyModel;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class RandomizerTest {
    private static List<PropertyModel> properties = TestUtils.getProperties();

    @Test
    public void randomizePropertyTest() {
        float randomnessIndex = 0.5F;
        PropertyRandomizerManager propertyRandomizerManager = new PropertyRandomizerManager(properties, randomnessIndex);
        Map<PropertyModel, PropertyRandomizeModel> randomizeMap = propertyRandomizerManager.getRandomizeMap();

        Assert.assertTrue(randomizeMap.isEmpty());
        propertyRandomizerManager.randomize();
        Assert.assertTrue(randomizeMap.size() > 0);

        PropertyRandomizeModel propertyRandomizeModel = new PropertyRandomizeModel(0.5F,
                0.5F,
                0.5F,
                0.5F,
                0.5F);

        Assert.assertEquals(0.5F, propertyRandomizeModel.getPricePercentage(), 0.01F);
        Assert.assertEquals(0.5F, propertyRandomizeModel.getMortgagePercentage(), 0.01F);
        Assert.assertEquals(0.5F, propertyRandomizeModel.getHousePricePercentage(), 0.01F);
        Assert.assertEquals(0.5F, propertyRandomizeModel.getHotelPricePercentage(), 0.01F);
        Assert.assertEquals(0.5F, propertyRandomizeModel.getRentPercentage(), 0.01F);

        Assert.assertFalse(randomizeMap.isEmpty());
        randomizeMap.clear();

        randomizeMap.put(properties.get(0), propertyRandomizeModel);

        Assert.assertEquals(1, randomizeMap.size());
        Assert.assertEquals(propertyRandomizeModel, propertyRandomizerManager.getPropertyRandomize(properties.get(0)));

        randomizeMap.clear();
        randomnessIndex = 1F;
        propertyRandomizerManager = new PropertyRandomizerManager(properties, randomnessIndex);
        propertyRandomizerManager.randomize();

        Assert.assertEquals(properties.size(), propertyRandomizerManager.getRandomizeMap().size());

        randomizeMap.clear();
        randomnessIndex = 0F;
        propertyRandomizerManager = new PropertyRandomizerManager(properties, randomnessIndex);
        propertyRandomizerManager.randomize();

        Assert.assertEquals(0, propertyRandomizerManager.getRandomizeMap().size());
    }

    @Test
    public void randomizeTaxTest() {
        float randomnessIndex = 0.5F;

        TaxRandomizerManager taxRandomizerManager = new TaxRandomizerManager(randomnessIndex);
        taxRandomizerManager.randomize();

        float min = taxRandomizerManager.getTaxesPercentage(), max = min;

        for (int i = 0; i < 100; i++) {
            taxRandomizerManager.randomize();
            if (taxRandomizerManager.getTaxesPercentage() < min) {
                min = taxRandomizerManager.getTaxesPercentage();
            } else if (taxRandomizerManager.getTaxesPercentage() > max) {
                max = taxRandomizerManager.getTaxesPercentage();
            }
        }

        Assert.assertTrue(min > -1);
        Assert.assertTrue(max < 5);
    }
}
