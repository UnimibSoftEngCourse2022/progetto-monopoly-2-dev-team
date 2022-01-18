package it.monopoly.controller.board;

import it.monopoly.controller.board.dispenser.*;
import it.monopoly.controller.board.space.Space;
import it.monopoly.model.property.PropertyModel;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final List<Space> gameboard;

    public Board(List<PropertyModel> properties) {
        this.gameboard = new ArrayList<>();

        SpaceDispenser chainOfResponsability = new TaxSpaceDispenser(4, 38);
        chainOfResponsability
                .setSuccessor(new ChanceSpaceDispenser(7, 22, 36))
                .setSuccessor(new CommunityChestSpaceDispenser(2, 17, 33))
                .setSuccessor(new GoToJailSpaceDispenser(30))
                .setSuccessor(new CornerSpaceDispenser(0, 10, 20))
                .setSuccessor(new PropertySpaceDispenser(properties));
        for (int i = 0; i < 40; i++) {
            gameboard.add(chainOfResponsability.processSpace(i));
        }
    }

    public List<Space> getGameboard() {
        return gameboard;
    }

    public Space getSpace(int position) {
        if (position > 0 && position < gameboard.size()) {
            return gameboard.get(position);
        }
        return null;
    }

}
