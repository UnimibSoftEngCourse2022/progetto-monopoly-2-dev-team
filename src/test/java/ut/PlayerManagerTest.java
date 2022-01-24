package ut;

import manager.player.PlayerManager;
import model.player.PlayerModel;
import model.player.PlayerState;
import model.player.Position;
import org.junit.Assert;
import org.junit.Test;
import util.Pair;

public class PlayerManagerTest extends BaseTest{

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
}
