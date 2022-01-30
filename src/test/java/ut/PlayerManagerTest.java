package ut;

import it.monopoly.controller.command.Command;
import it.monopoly.controller.player.command.LoyaltyProgramCommandBuilder;
import it.monopoly.controller.player.command.PayRentCommandBuilder;
import it.monopoly.manager.loyaltyprogram.LoyaltyProgram;
import it.monopoly.manager.player.PlayerManager;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.player.PlayerState;
import it.monopoly.model.player.Position;
import it.monopoly.model.property.PropertyModel;
import it.monopoly.util.Pair;
import org.junit.Assert;
import org.junit.Test;

public class PlayerManagerTest extends BaseTest {

    @Test
    public void playerManagerTest() {
        PlayerManager playerManager = new PlayerManager(
                new PlayerModel("id", "name"),
                1000,
                ownerMapper
        );
        playerManager.earn(100);
        Assert.assertEquals(1100, playerManager.getFunds());
        playerManager.spend(100);
        Assert.assertEquals(PlayerState.FREE, playerManager.getState());
        Assert.assertEquals(1000, playerManager.getFunds());
        playerManager.spend(1100);
        Assert.assertEquals(PlayerState.BANKRUPT, playerManager.getState());
        Assert.assertFalse(playerManager.canTakeTurn());
        Assert.assertFalse(playerManager.canMove());
        playerManager.earn(10000);

        playerManager.goToJail();
        Assert.assertEquals(PlayerState.IN_JAIL, playerManager.getState());
        Assert.assertTrue(playerManager.isInJail());
        Assert.assertTrue(playerManager.canTakeTurn());
        Assert.assertFalse(playerManager.canMove());

        playerManager.getOutOfJailWithFine();
        Assert.assertEquals(PlayerState.FINED, playerManager.getState());
        Assert.assertFalse(playerManager.isInJail());
        playerManager.getOutOfJail();
        Assert.assertEquals(PlayerState.FREE, playerManager.getState());

        Position position = playerManager.move(6, false);
        Assert.assertEquals(6, position.getIntPosition());
        Assert.assertEquals(new Pair<>(0, 6), position.getLastMovement().getMovement());
        Assert.assertFalse(position.getLastMovement().isDirect());
        position = playerManager.move(13, true);
        Assert.assertEquals(19, position.getIntPosition());
        Assert.assertEquals(new Pair<>(6, 19), position.getLastMovement().getMovement());
        Assert.assertTrue(position.getLastMovement().isDirect());
    }

    @Test
    public void loyaltyProgramPercentageTest() {

        resetPlayers();
        PlayerModel debtor = players.get(0);
        PlayerModel creditor = players.get(1);

        PropertyModel property = properties.get(21);

        ownerMapper.setOwner(property, creditor);
        property.setHouseNumber(1);

        Assert.assertNull(playerController.getManager(debtor).getLoyaltyProgram());

        Command joinLoyaltyProgramCommand =
                commandBuilderDispatcher
                        .createCommandBuilder(LoyaltyProgramCommandBuilder.class)
                        .setDebtor(debtor)
                        .setCreditor(creditor)
                        .setType(LoyaltyProgram.Type.PERCENTAGE)
                        .build();

        Assert.assertTrue(joinLoyaltyProgramCommand.isEnabled());

        joinLoyaltyProgramCommand.execute();

        LoyaltyProgram loyaltyProgram = playerController.getManager(debtor).getLoyaltyProgram();

        Assert.assertNotNull(loyaltyProgram);
        Assert.assertEquals(creditor, loyaltyProgram.getCreditor());

        // first time using loyalty program
        Command payRentCommand = commandBuilderDispatcher
                .createCommandBuilder(PayRentCommandBuilder.class)
                .setPlayer(debtor)
                .setProperty(property)
                .build();

        int funds = playerController.getManager(debtor).getFunds();

        payRentCommand.execute();

        Assert.assertEquals(funds - 190, playerController.getManager(debtor).getFunds());

        // second time using loyalty program
        payRentCommand = commandBuilderDispatcher
                .createCommandBuilder(PayRentCommandBuilder.class)
                .setPlayer(debtor)
                .setProperty(property)
                .build();

        funds = playerController.getManager(debtor).getFunds();

        payRentCommand.execute();

        Assert.assertEquals(funds - 150, playerController.getManager(debtor).getFunds());

        // max percentage
        payRentCommand = commandBuilderDispatcher
                .createCommandBuilder(PayRentCommandBuilder.class)
                .setPlayer(debtor)
                .setProperty(property)
                .build();

        funds = playerController.getManager(debtor).getFunds();

        payRentCommand.execute();

        Assert.assertEquals(funds - 150, playerController.getManager(debtor).getFunds());

        // players who didn't join a loyalt program always pay the same
        PlayerModel debtor2 = players.get(2);

        payRentCommand = commandBuilderDispatcher
                .createCommandBuilder(PayRentCommandBuilder.class)
                .setPlayer(debtor2)
                .setProperty(property)
                .build();

        int funds2 = playerController.getManager(debtor2).getFunds();

        payRentCommand.execute();

        Assert.assertEquals(funds2 - 200, playerController.getManager(debtor2).getFunds());

        payRentCommand = commandBuilderDispatcher
                .createCommandBuilder(PayRentCommandBuilder.class)
                .setPlayer(debtor2)
                .setProperty(property)
                .build();

        funds2 = playerController.getManager(debtor2).getFunds();

        payRentCommand.execute();

        Assert.assertEquals(funds2 - 200, playerController.getManager(debtor2).getFunds());
    }

    @Test
    public void loyaltyProgramPointsTest() {

        resetPlayers();
        PlayerModel debtor = players.get(0);
        PlayerModel creditor = players.get(1);

        PropertyModel property = properties.get(21);

        ownerMapper.setOwner(property, creditor);
        property.setHouseNumber(1);

        Assert.assertNull(playerController.getManager(debtor).getLoyaltyProgram());

        Command joinLoyaltyProgramCommand =
                commandBuilderDispatcher
                        .createCommandBuilder(LoyaltyProgramCommandBuilder.class)
                        .setDebtor(debtor)
                        .setCreditor(creditor)
                        .setType(LoyaltyProgram.Type.POINTS)
                        .build();

        Assert.assertTrue(joinLoyaltyProgramCommand.isEnabled());

        joinLoyaltyProgramCommand.execute();

        LoyaltyProgram loyaltyProgram = playerController.getManager(debtor).getLoyaltyProgram();

        Assert.assertNotNull(loyaltyProgram);
        Assert.assertEquals(creditor, loyaltyProgram.getCreditor());

        // first time using loyalty program
        Command payRentCommand = commandBuilderDispatcher
                .createCommandBuilder(PayRentCommandBuilder.class)
                .setPlayer(debtor)
                .setProperty(property)
                .build();

        int funds = playerController.getManager(debtor).getFunds();

        payRentCommand.execute();

        Assert.assertEquals(funds - 200, playerController.getManager(debtor).getFunds());
        Assert.assertEquals("20", loyaltyProgram.getValue());

        // second time using loyalty program
        payRentCommand = commandBuilderDispatcher
                .createCommandBuilder(PayRentCommandBuilder.class)
                .setPlayer(debtor)
                .setProperty(property)
                .build();

        funds = playerController.getManager(debtor).getFunds();

        payRentCommand.execute();

        Assert.assertEquals(funds - 200, playerController.getManager(debtor).getFunds());
        Assert.assertEquals("40", loyaltyProgram.getValue());

        // simil PayRentCommand
        funds = playerController.getManager(debtor).getFunds();

        // use points case
        playerController.getManager(debtor).spend(loyaltyProgram.spendSales(creditor, property.getHouseRent()));
        Assert.assertEquals(funds - 160, playerController.getManager(debtor).getFunds());
        Assert.assertEquals("0", loyaltyProgram.getValue());

        // use points case - points > rent
        funds = playerController.getManager(debtor).getFunds();

        commandBuilderDispatcher
                .createCommandBuilder(PayRentCommandBuilder.class)
                .setPlayer(debtor)
                .setProperty(property)
                .build()
                .execute();

        Assert.assertEquals(funds - 200, playerController.getManager(debtor).getFunds());
        Assert.assertEquals("20", loyaltyProgram.getValue());

        funds = playerController.getManager(debtor).getFunds();

        PropertyModel property2 = properties.get(0);
        ownerMapper.setOwner(property2, creditor);

        playerController.getManager(debtor).spend(loyaltyProgram.spendSales(creditor, property2.getBaseRent()));

        Assert.assertEquals(funds, playerController.getManager(debtor).getFunds());
        Assert.assertEquals("18", loyaltyProgram.getValue());
    }
}
