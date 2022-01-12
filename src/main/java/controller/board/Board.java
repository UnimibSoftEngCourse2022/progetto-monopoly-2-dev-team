package controller.board;

import controller.board.dispenser.*;
import controller.board.space.Space;
import model.property.PropertyModel;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final List<Space> gameboard;

    public Board(List<PropertyModel> properties) {
        this.gameboard = new ArrayList<>();

        SpaceDispenser chainOfResponsability = new TaxSpaceDispenser(4, 38)
                .setSuccessor(new ChanceSpaceDispenser(7, 22, 36))
                .setSuccessor(new CommunityChestSpaceDispenser(2, 17, 33))
                .setSuccessor(new GoToJailSpaceDispenser(30))
                .setSuccessor(new CornerSpaceDispenser(10, 20, 40))
                .setSuccessor(new PropertySpaceDispenser(properties));
        for (int i = 0; i < 40; i++) {
            gameboard.add(chainOfResponsability.processSpace(i));
        }
    }

}
