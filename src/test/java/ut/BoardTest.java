package ut;

import controller.board.Board;
import controller.board.space.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class BoardTest {

    private static Board board = new Board(TestUtils.getProperties());

    @Test
    public void chainOfResponsabilityTest() {
        List<Space> boardSpaces = board.getGameboard();

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
        Assert.assertEquals("Baltic Avenue", ((PropertySpace) board.getSpace(3)).getProperty().getName());
    }
}
