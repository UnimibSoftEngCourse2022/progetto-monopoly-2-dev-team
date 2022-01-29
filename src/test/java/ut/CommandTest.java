package ut;

import it.monopoly.controller.TradeController;
import it.monopoly.controller.board.card.DrawableCardController;
import it.monopoly.controller.command.Command;
import it.monopoly.controller.command.CommandBuilderDispatcher;
import it.monopoly.controller.command.MainCommandBuilderDispatcher;
import it.monopoly.controller.event.DiceRoller;
import it.monopoly.controller.event.EventDispatcher;
import it.monopoly.controller.event.callback.BuyOrAuctionCallback;
import it.monopoly.controller.event.callback.DiceRollEventCallback;
import it.monopoly.controller.event.callback.UsePointsCallback;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.controller.player.command.DiceRollCommandBuilder;
import it.monopoly.controller.player.command.PayCommandBuilder;
import it.monopoly.controller.player.command.PayRentCommandBuilder;
import it.monopoly.controller.property.PropertyController;
import it.monopoly.controller.property.command.PropertyCommandBuilder;
import it.monopoly.manager.AbstractOfferManager;
import it.monopoly.manager.player.PlayerManager;
import it.monopoly.manager.pricemanager.PriceManagerDispatcher;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyCategory;
import it.monopoly.model.property.PropertyModel;
import it.monopoly.model.property.ReadablePropertyModel;
import it.monopoly.util.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static it.monopoly.model.player.PlayerState.*;

public class CommandTest extends BaseTest {
    public static PlayerController playerController;
    public static PropertyController propertyController;
    public static CommandBuilderDispatcher commandBuilderDispatcher;
    public static DiceRoller diceRoller;

    @Before
    public void init() {
        super.init();
        EventDispatcher eventDispatcher = new EventDispatcher() {
            @Override
            public DiceRoller diceRollEvent() {
                return diceRoller;
            }

            @Override
            public DiceRoller diceRollEvent(DiceRollEventCallback callback) {
                return diceRoller;
            }

            @Override
            public void startOffer(AbstractOfferManager offerManager) {

            }

            @Override
            public void buyOrAuction(PlayerModel player, ReadablePropertyModel propertyModel, BuyOrAuctionCallback callback) {
            }

            @Override
            public void useLoyaltyPoints(PlayerModel player, UsePointsCallback usePointsCallback) {

            }

            @Override
            public void showDialog(PlayerModel player, String message) {

            }

            @Override
            public void sendMessage(String message) {
            }
        };
        playerController = new PlayerController(players, ownerMapper);
        PriceManagerDispatcher priceManagerDispatcher = new PriceManagerDispatcher(null, ownerMapper, categoryMapper, eventDispatcher.diceRollEvent());
        propertyController = new PropertyController(properties, priceManagerDispatcher, ownerMapper, categoryMapper);
        commandBuilderDispatcher = new MainCommandBuilderDispatcher(
                propertyController,
                playerController,
                new DrawableCardController(false),
                new TradeController(playerController, propertyController),
                eventDispatcher
        );
    }

    @Test
    public void propertyCommandsTest() {
        PlayerModel player = players.get(0);
        PropertyModel firstBrownProperty = categoryMapper.getCategoryProperties(PropertyCategory.BROWN).get(0);

        Command buildCommand = commandBuilderDispatcher
                .createCommandBuilder(PropertyCommandBuilder.class)
                .setProperty(firstBrownProperty)
                .setType(PropertyCommandBuilder.Type.BUILD)
                .build();

        Assert.assertFalse(buildCommand.isEnabled());

        for (PropertyModel categoryProperty : categoryMapper.getCategoryProperties(PropertyCategory.BROWN)) {
            propertyController.getManager(categoryProperty).setOwner(player);
        }

        buildCommand = commandBuilderDispatcher
                .createCommandBuilder(PropertyCommandBuilder.class)
                .setProperty(firstBrownProperty)
                .setType(PropertyCommandBuilder.Type.BUILD)
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
                .setType(PropertyCommandBuilder.Type.BUILD)
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
                .setType(PropertyCommandBuilder.Type.SELL)
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
                .setType(PropertyCommandBuilder.Type.SELL)
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

    @Test
    public void diceRollCommandsTest() {
        PlayerModel player = players.get(0);
        PlayerManager playerManager = playerController.getManager(player);

        Assert.assertEquals(FREE, playerManager.getState());
        Assert.assertTrue(playerManager.canTakeTurn());
        Assert.assertTrue(playerManager.canMove());
        Assert.assertEquals(0, playerManager.getPosition().getIntPosition());

        Command rollCommand = commandBuilderDispatcher
                .createCommandBuilder(DiceRollCommandBuilder.class)
                .setPlayer(player)
                .build();

        Assert.assertTrue(rollCommand.isEnabled());


        diceRoller = () -> new Pair<>(5, 6);
        rollCommand.execute();

        // THROW case
        Assert.assertEquals(11, playerManager.getPosition().getIntPosition());

        diceRoller = () -> {
            diceRoller = () -> new Pair<>(2, 3);
            return new Pair<>(6, 6);
        };
        rollCommand.execute();

        // DOUBLEs case
        Assert.assertEquals(28, playerManager.getPosition().getIntPosition());
        Assert.assertEquals(FREE, playerManager.getState());
        Assert.assertTrue(playerManager.canTakeTurn());
        Assert.assertTrue(playerManager.canMove());

        diceRoller = () -> new Pair<>(1, 1);
        rollCommand.execute();

        // IN JAIL case
        //Assert.assertEquals(10, playerManager.getPosition().getIntPosition());
        Assert.assertEquals(IN_JAIL, playerManager.getState());
        Assert.assertTrue(playerManager.canTakeTurn());
        Assert.assertFalse(playerManager.canMove());

        rollCommand = commandBuilderDispatcher
                .createCommandBuilder(DiceRollCommandBuilder.class)
                .setPlayer(player)
                .build();

        int funds = playerManager.getFunds();
        int position = playerManager.getPosition().getIntPosition();
        diceRoller = () -> new Pair<>(1, 2);

        // TRIPLE THROW IN JAIL
        for (int i = 0; i < 3; i++) {
            rollCommand.execute();
        }

        Assert.assertEquals(funds - 50, playerManager.getFunds());
        Assert.assertEquals(FREE, playerManager.getState());
        Assert.assertTrue(playerManager.canTakeTurn());
        Assert.assertTrue(playerManager.canMove());
        Assert.assertEquals(position + 3, playerManager.getPosition().getIntPosition());

        // DOUBLE throw in JAIL
        playerManager.setState(IN_JAIL);

        rollCommand = commandBuilderDispatcher
                .createCommandBuilder(DiceRollCommandBuilder.class)
                .setPlayer(player)
                .build();

        funds = playerManager.getFunds();
        position = playerManager.getPosition().getIntPosition();
        diceRoller = () -> new Pair<>(1, 1);

        rollCommand.execute();

        Assert.assertEquals(funds, playerManager.getFunds());
        Assert.assertEquals(FREE, playerManager.getState());
        Assert.assertTrue(playerManager.canTakeTurn());
        Assert.assertTrue(playerManager.canMove());
        Assert.assertEquals(position + 2, playerManager.getPosition().getIntPosition());

        // Player FINED
        playerManager.setState(FINED);

        rollCommand = commandBuilderDispatcher
                .createCommandBuilder(DiceRollCommandBuilder.class)
                .setPlayer(player)
                .build();

        funds = playerManager.getFunds();
        playerManager.getPosition().setPosition(0);
        diceRoller = () -> new Pair<>(1, 2);

        rollCommand.execute();

        Assert.assertEquals(funds - 50, playerManager.getFunds());
        Assert.assertEquals(FINED, playerManager.getState());
        Assert.assertTrue(playerManager.canTakeTurn());
        Assert.assertTrue(playerManager.canMove());
        Assert.assertEquals(3, playerManager.getPosition().getIntPosition());

        /*
        rollCommand = commandBuilderDispatcher
                .createCommandBuilder(DiceRollCommandBuilder.class)
                .setPlayer(player)
                .build();

        funds = playerManager.getFunds();
        position = playerManager.getPosition().getIntPosition();
        diceRoller = () -> new Pair<>(1, 2);

        rollCommand.execute();

        Assert.assertEquals(funds - 50, playerManager.getFunds());
        Assert.assertEquals(FREE, playerManager.getState());
        Assert.assertTrue(playerManager.canTakeTurn());
        Assert.assertTrue(playerManager.canMove());
        Assert.assertEquals(position + 3, playerManager.getPosition().getIntPosition());
        */


    }

    @Test
    public void paymentsCommandsTest() {

        resetPlayers();

        PlayerManager playerManager1 = playerController.getManager(players.get(0));
        PlayerManager playerManager2 = playerController.getManager(players.get(1));

        // Player payments
        int funds1 = playerManager1.getFunds();
        int funds2 = playerManager2.getFunds();

        Command payCommand = commandBuilderDispatcher
                .createCommandBuilder(PayCommandBuilder.class)
                .addCreditor(playerManager1.getModel())
                .addDebtor(playerManager2.getModel())
                .setMoney(50)
                .build();

        payCommand.execute();

        Assert.assertEquals(funds1 + 50, playerManager1.getFunds());
        Assert.assertEquals(funds2 - 50, playerManager2.getFunds());

    }

    @Test
    public void payRentCommandTest() {
        PlayerModel player = players.get(0);
        PlayerManager playerManager = playerController.getManager(player);
        PropertyModel firstBrownProperty = categoryMapper.getCategoryProperties(PropertyCategory.BROWN).get(0);

        Command payRentCommand = commandBuilderDispatcher
                .createCommandBuilder(PayRentCommandBuilder.class)
                .setProperty(firstBrownProperty)
                .setPlayer(player)
                .build();

        payRentCommand.execute();

        // Property not selled yet
        Assert.assertFalse(payRentCommand.isEnabled());

        payRentCommand = commandBuilderDispatcher
                .createCommandBuilder(PayRentCommandBuilder.class)
                .setProperty(firstBrownProperty)
                .setPlayer(player)
                .build();

        // Property rented
        propertyController.getManager(firstBrownProperty).setOwner(players.get(2));
        int funds = playerManager.getFunds();

        Assert.assertTrue(payRentCommand.isEnabled());

        payRentCommand.execute();

        Assert.assertEquals(funds - 2, playerManager.getFunds());

        // Property mortgaged
        propertyController.getManager(firstBrownProperty).mortgage();

        payRentCommand = commandBuilderDispatcher
                .createCommandBuilder(PayRentCommandBuilder.class)
                .setProperty(firstBrownProperty)
                .setPlayer(player)
                .build();

        Assert.assertFalse(payRentCommand.isEnabled());

        payRentCommand.execute();

    }
}
