package ut;

import it.monopoly.controller.board.Board;
import it.monopoly.controller.board.card.DrawableCardController;
import it.monopoly.controller.board.space.*;
import it.monopoly.controller.player.command.GetOutOfJailCommand;
import it.monopoly.manager.player.PlayerManager;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.player.PlayerState;
import it.monopoly.model.property.PropertyCategory;
import it.monopoly.model.property.PropertyModel;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static it.monopoly.model.player.PlayerState.FREE;
import static it.monopoly.model.player.PlayerState.IN_JAIL;

public class BoardTest extends BaseTest {

    private static Board board;

    @Test
    public void chainOfResponsabilityTest() {
        board = new Board(commandBuilderDispatcher, eventDispatcher, playerController, propertyController);
        List<Space> boardSpaces = board.getSpaces();

        Assert.assertNotNull(boardSpaces);

        Assert.assertTrue(boardSpaces.get(0) instanceof CornerSpace);
        Assert.assertTrue(boardSpaces.get(2) instanceof CommunityChestSpace);
        Assert.assertTrue(boardSpaces.get(4) instanceof TaxSpace);
        Assert.assertTrue(boardSpaces.get(7) instanceof ChanceSpace);
        Assert.assertTrue(boardSpaces.get(30) instanceof GoToJailSpace);

        PropertySpace propertySpace;

        Assert.assertTrue(boardSpaces.get(1) instanceof PropertySpace);
        propertySpace = (PropertySpace) boardSpaces.get(1);
        Assert.assertEquals("Mediterranean Avenue", propertySpace.getProperty().getName());

        Assert.assertTrue(boardSpaces.get(3) instanceof PropertySpace);
        propertySpace = (PropertySpace) board.getSpace(3);
        Assert.assertEquals("Baltic Avenue", propertySpace.getProperty().getName());
    }

    @Test
    public void taxSpacesTest() {
        resetPlayers();
        PlayerModel player = players.get(0);

        TaxSpace taxSpace = new TaxSpace(commandBuilderDispatcher, 100);

        int funds = playerController.getManager(player).getFunds();

        taxSpace.applyEffect(player);

        Assert.assertEquals(funds - 100, playerController.getManager(player).getFunds());
    }

    @Test
    public void cornerSpaceTest() {
        resetPlayers();
        PlayerModel player = players.get(0);

        CornerSpace cornerSpace = new CornerSpace(commandBuilderDispatcher);

        cornerSpace.applyEffect(player);

        Assert.assertEquals(player, players.get(0));
    }

    @Test
    public void goToJailSpaceTest() {
        resetPlayers();
        PlayerModel player = players.get(0);
        PlayerState state = playerController.getManager(player).getState();

        Assert.assertEquals(PlayerState.FREE, state);

        GoToJailSpace goToJailSpace = new GoToJailSpace(commandBuilderDispatcher, playerController);

        goToJailSpace.applyEffect(player);

        state = playerController.getManager(player).getState();

        Assert.assertEquals(IN_JAIL, state);
    }

    @Test
    public void chanceSpaceTest() {
        resetPlayers();
        PlayerModel player = players.get(0);

        DrawableCardController drawableCardController = new DrawableCardController(false);

        ChanceSpace chanceSpace = new ChanceSpace(commandBuilderDispatcher, drawableCardController, playerController, eventDispatcher);

        PlayerManager playerManager = playerController.getManager(player);

        playerManager.getPosition().setPosition(7);

        int funds = playerManager.getFunds();
        int position;

        // move_to card
        chanceSpace.applyEffect(player);

        Assert.assertEquals(0, playerManager.getPosition().getIntPosition());
        Assert.assertEquals(funds + 200, playerManager.getFunds());

        // goToJail card
        funds = playerManager.getFunds();

        chanceSpace.applyEffect(player);

        Assert.assertEquals(IN_JAIL, playerManager.getState());
        Assert.assertEquals(10, playerManager.getPosition().getIntPosition());
        Assert.assertEquals(funds, playerManager.getFunds());

        playerManager.getOutOfJail();
        // move_of card
        position = playerManager.getPosition().getIntPosition();

        chanceSpace.applyEffect(player);

        Assert.assertEquals(position - 3, playerManager.getPosition().getIntPosition());

        // move_near card
        playerManager.getPosition().setPosition(39);

        chanceSpace.applyEffect(player);

        Assert.assertEquals(5, playerManager.getPosition().getIntPosition());

        // pay - player creditor
        funds = playerManager.getFunds();

        chanceSpace.applyEffect(player);

        Assert.assertEquals(funds + 50, playerManager.getFunds());

        // pay - player debtor
        funds = playerManager.getFunds();

        chanceSpace.applyEffect(player);

        Assert.assertEquals(funds - 15, playerManager.getFunds());

        // pay - player debtor - others creditor
        PlayerModel player2 = players.get(1);
        PlayerModel player3 = players.get(2);
        PlayerModel player4 = players.get(3);

        int funds2 = playerController.getManager(player2).getFunds();
        int funds3 = playerController.getManager(player3).getFunds();
        int funds4 = playerController.getManager(player4).getFunds();

        funds = playerManager.getFunds();

        chanceSpace.applyEffect(player);

        Assert.assertEquals(funds - 150, playerManager.getFunds());
        Assert.assertEquals(funds2 + 50, playerController.getManager(player2).getFunds());
        Assert.assertEquals(funds3 + 50, playerController.getManager(player3).getFunds());
        Assert.assertEquals(funds4 + 50, playerController.getManager(player4).getFunds());

        // pay - player's house and hotels
        PropertyModel propertyModel1 = properties.get(0);
        PropertyModel propertyModel2 = properties.get(1);
        ownerMapper.setOwner(propertyModel1, player);
        ownerMapper.setOwner(propertyModel2, player);
        propertyModel1.setHouseNumber(4);
        propertyModel2.setHotelNumber(1);

        funds = playerManager.getFunds();

        chanceSpace.applyEffect(player);

        Assert.assertEquals(funds - 4 * 25 - 100, playerManager.getFunds());

        // keep - get out of jail card
        Assert.assertEquals(0, playerManager.getDrawableCardModels().size());
        Assert.assertEquals(FREE, playerManager.getState());

        chanceSpace.applyEffect(player);

        playerManager.goToJail();

        Assert.assertEquals(IN_JAIL, playerManager.getState());
        Assert.assertEquals(1, playerManager.getDrawableCardModels().size());

        GetOutOfJailCommand getOutOfJailCommand = new GetOutOfJailCommand(playerController, drawableCardController, player);
        getOutOfJailCommand.execute();

        Assert.assertEquals(FREE, playerManager.getState());
        Assert.assertEquals(0, playerManager.getDrawableCardModels().size());

    }

    @Test
    public void communityChestSpaceTest() {
        resetPlayers();
        PlayerModel player = players.get(0);

        DrawableCardController drawableCardController = new DrawableCardController(false);
        PlayerManager playerManager = playerController.getManager(player);

        CommunityChestSpace communityChestSpace = new CommunityChestSpace(commandBuilderDispatcher, drawableCardController, playerController, eventDispatcher);

        playerManager.getPosition().setPosition(3);
        int funds = playerManager.getFunds();

        // move_to card
        communityChestSpace.applyEffect(player);

        Assert.assertEquals(0, playerManager.getPosition().getIntPosition());
        Assert.assertEquals(funds + 200, playerManager.getFunds());

        // goToJail card
        funds = playerManager.getFunds();

        communityChestSpace.applyEffect(player);

        Assert.assertEquals(IN_JAIL, playerManager.getState());
        Assert.assertEquals(10, playerManager.getPosition().getIntPosition());
        Assert.assertEquals(funds, playerManager.getFunds());

        playerManager.getOutOfJail();
        // pay - player creditor
        funds = playerManager.getFunds();

        communityChestSpace.applyEffect(player);

        Assert.assertEquals(funds + 200, playerManager.getFunds());

        // pay - player debtor
        funds = playerManager.getFunds();

        communityChestSpace.applyEffect(player);

        Assert.assertEquals(funds - 50, playerManager.getFunds());

        // pay - player debtor - others creditor
        PlayerModel player2 = players.get(1);
        PlayerModel player3 = players.get(2);
        PlayerModel player4 = players.get(3);

        int funds2 = playerController.getManager(player2).getFunds();
        int funds3 = playerController.getManager(player3).getFunds();
        int funds4 = playerController.getManager(player4).getFunds();

        funds = playerManager.getFunds();

        communityChestSpace.applyEffect(player);

        Assert.assertEquals(funds + 150, playerManager.getFunds());
        Assert.assertEquals(funds2 - 50, playerController.getManager(player2).getFunds());
        Assert.assertEquals(funds3 - 50, playerController.getManager(player3).getFunds());
        Assert.assertEquals(funds4 - 50, playerController.getManager(player4).getFunds());

        // pay - player's house and hotels
        PropertyModel propertyModel1 = properties.get(0);
        PropertyModel propertyModel2 = properties.get(1);
        ownerMapper.setOwner(propertyModel1, player);
        ownerMapper.setOwner(propertyModel2, player);
        propertyModel1.setHouseNumber(4);
        propertyModel2.setHotelNumber(1);

        funds = playerManager.getFunds();

        communityChestSpace.applyEffect(player);

        Assert.assertEquals(funds - 4 * 40 - 115, playerManager.getFunds());

        // keep - get out of jail card
        Assert.assertEquals(0, playerManager.getDrawableCardModels().size());
        Assert.assertEquals(FREE, playerManager.getState());

        communityChestSpace.applyEffect(player);

        playerManager.goToJail();

        Assert.assertEquals(IN_JAIL, playerManager.getState());
        Assert.assertEquals(1, playerManager.getDrawableCardModels().size());

        GetOutOfJailCommand getOutOfJailCommand = new GetOutOfJailCommand(playerController, drawableCardController, player);
        getOutOfJailCommand.execute();

        Assert.assertEquals(FREE, playerManager.getState());
        Assert.assertEquals(0, playerManager.getDrawableCardModels().size());
    }

    @Test
    public void propertySpaceTest() {
        resetPlayers();
        PlayerModel player1 = players.get(0);
        PlayerModel player2 = players.get(1);

        PlayerManager playerManager1 = playerController.getManager(player1);
        PlayerManager playerManager2 = playerController.getManager(player2);

        int funds1;
        int funds2;

        PropertyModel property1 = categoryMapper.getCategoryProperties(PropertyCategory.BROWN).get(0);
        PropertyModel property2 = categoryMapper.getCategoryProperties(PropertyCategory.BROWN).get(1);
        PropertyModel property3 = categoryMapper.getCategoryProperties(PropertyCategory.ORANGE).get(0);

        ownerMapper.setOwner(property1, player1);
        ownerMapper.setOwner(property2, player2);


        PropertySpace propertySpace1 = new PropertySpace(commandBuilderDispatcher, propertyController.getManager(property1));
        PropertySpace propertySpace2 = new PropertySpace(commandBuilderDispatcher, propertyController.getManager(property2));
        PropertySpace propertySpace3 = new PropertySpace(commandBuilderDispatcher, propertyController.getManager(property3));

        // player on his property
        funds1 = playerManager1.getFunds();

        propertySpace1.applyEffect(player1);

        Assert.assertEquals(funds1, playerManager1.getFunds());

        // player on others' property
        funds1 = playerManager1.getFunds();
        funds2 = playerManager2.getFunds();

        propertySpace2.applyEffect(player1);

        Assert.assertEquals(funds1 - 4, playerManager1.getFunds());
        Assert.assertEquals(funds2 + 4, playerManager2.getFunds());

        // player on no men's property
        funds1 = playerManager1.getFunds();

        propertySpace3.applyEffect(player1);

        Assert.assertEquals(funds1, playerManager1.getFunds());
    }
}
