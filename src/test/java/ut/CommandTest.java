package ut;

import controller.TradeController;
import controller.command.Command;
import controller.command.CommandBuilderDispatcher;
import controller.command.MainCommandBuilderDispatcher;
import controller.event.EventDispatcher;
import controller.player.PlayerController;
import controller.property.PropertyController;
import controller.property.command.PropertyCommandBuilder;
import manager.player.PlayerManager;
import model.player.PlayerModel;
import model.property.PropertyCategory;
import model.property.PropertyModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CommandTest extends BaseTest {
    public static PlayerController playerController;
    public static PropertyController propertyController;
    public static CommandBuilderDispatcher commandBuilderDispatcher;

    @Before
    public void init() {
        super.init();
        playerController = new PlayerController(players, ownerMapper);
        propertyController = new PropertyController(properties, ownerMapper, categoryMapper);
        commandBuilderDispatcher = new MainCommandBuilderDispatcher(
                propertyController,
                playerController,
                new TradeController(playerController, propertyController),
                new EventDispatcher()
        );
    }

    @Test
    public void propertyCommandsTest() {
        PlayerModel player = players.get(0);
        PropertyModel firstBrownProperty = categoryMapper.getCategoryProperties(PropertyCategory.BROWN).get(0);

        Command buildCommand = commandBuilderDispatcher
                .createCommandBuilder(PropertyCommandBuilder.class)
                .setProperty(firstBrownProperty)
                .setType(PropertyCommandBuilder.Type.BUILD_HOUSE)
                .build();

        Assert.assertFalse(buildCommand.isEnabled());

        for (PropertyModel categoryProperty : categoryMapper.getCategoryProperties(PropertyCategory.BROWN)) {
            propertyController.getManager(categoryProperty).setOwner(player);
        }

        buildCommand = commandBuilderDispatcher
                .createCommandBuilder(PropertyCommandBuilder.class)
                .setProperty(firstBrownProperty)
                .setType(PropertyCommandBuilder.Type.BUILD_HOUSE)
                .build();

        Assert.assertTrue(buildCommand.isEnabled());

        PlayerManager playerManager = playerController.getManager(player);
        int funds = playerManager.getFunds();
        buildCommand.execute();

        Assert.assertEquals(funds - 50, playerManager.getFunds());

        for (PropertyModel categoryProperty : categoryMapper.getCategoryProperties(PropertyCategory.BROWN)) {
            categoryProperty.setHouseNumber(4);
        }

        funds = playerManager.getFunds();
        buildCommand = commandBuilderDispatcher
                .createCommandBuilder(PropertyCommandBuilder.class)
                .setProperty(firstBrownProperty)
                .setType(PropertyCommandBuilder.Type.BUILD_HOTEL)
                .build();

        Assert.assertTrue(buildCommand.isEnabled());

        buildCommand.execute();

        Assert.assertEquals(funds - 50, playerManager.getFunds());
        Assert.assertEquals(1, firstBrownProperty.getHotelNumber());
        Assert.assertEquals(0, firstBrownProperty.getHouseNumber());

        funds = playerManager.getFunds();
        buildCommand = commandBuilderDispatcher
                .createCommandBuilder(PropertyCommandBuilder.class)
                .setProperty(firstBrownProperty)
                .setType(PropertyCommandBuilder.Type.SELL_HOTEL)
                .build();

        Assert.assertTrue(buildCommand.isEnabled());

        buildCommand.execute();

        Assert.assertEquals(funds + 25, playerManager.getFunds());
        Assert.assertEquals(0, firstBrownProperty.getHotelNumber());
        Assert.assertEquals(4, firstBrownProperty.getHouseNumber());

        funds = playerManager.getFunds();
        buildCommand = commandBuilderDispatcher
                .createCommandBuilder(PropertyCommandBuilder.class)
                .setProperty(firstBrownProperty)
                .setType(PropertyCommandBuilder.Type.SELL_HOUSE)
                .build();

        Assert.assertTrue(buildCommand.isEnabled());

        buildCommand.execute();

        Assert.assertEquals(funds + 25, playerManager.getFunds());
        Assert.assertEquals(3, firstBrownProperty.getHouseNumber());

        // can't sell house if (3, 4 ,4)
        Assert.assertFalse(buildCommand.isEnabled());

        // mortgage tests
        // can't mortgage property if buildings are built on it
        funds = playerManager.getFunds();
        buildCommand = commandBuilderDispatcher
                .createCommandBuilder(PropertyCommandBuilder.class)
                .setProperty(firstBrownProperty)
                .setType(PropertyCommandBuilder.Type.MORTGAGE)
                .build();

        Assert.assertFalse(buildCommand.isEnabled());

        for (PropertyModel categoryProperty : categoryMapper.getCategoryProperties(PropertyCategory.BROWN)) {
            categoryProperty.setHouseNumber(0);
            categoryProperty.setHotelNumber(1);
        }

        Assert.assertFalse(buildCommand.isEnabled());

        for (PropertyModel categoryProperty : categoryMapper.getCategoryProperties(PropertyCategory.BROWN)) {
            categoryProperty.setHotelNumber(0);
        }

        funds = playerManager.getFunds();
        buildCommand = commandBuilderDispatcher
                .createCommandBuilder(PropertyCommandBuilder.class)
                .setProperty(firstBrownProperty)
                .setType(PropertyCommandBuilder.Type.MORTGAGE)
                .build();

        Assert.assertTrue(buildCommand.isEnabled());
        buildCommand.execute();

        Assert.assertEquals(funds + 30, playerManager.getFunds());
        Assert.assertEquals(player, propertyController.getManager(categoryMapper.getCategoryProperties(PropertyCategory.BROWN).get(0)).getOwner());

    }
}
