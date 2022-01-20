package controller.board;

import controller.board.dispenser.*;
import controller.board.space.Space;
import controller.command.CommandBuilderDispatcher;
import controller.card.DrawableCardController;
import controller.player.PlayerController;
import model.property.PropertyModel;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final List<Space> spaces;

    public Board(CommandBuilderDispatcher commandBuilderDispatcher,
                 PlayerController playerController,
                 List<PropertyModel> properties) {
        this.spaces = new ArrayList<>();

        DrawableCardController drawableCardController = new DrawableCardController(true);

        SpaceDispenser chainOfResponsability = new TaxSpaceDispenser(commandBuilderDispatcher, 4, 38);
        chainOfResponsability
                .setSuccessor(new ChanceSpaceDispenser(commandBuilderDispatcher, drawableCardController, playerController, 7, 22, 36))
                .setSuccessor(new CommunityChestSpaceDispenser(commandBuilderDispatcher, drawableCardController, playerController, 2, 17, 33))
                .setSuccessor(new GoToJailSpaceDispenser(commandBuilderDispatcher, playerController, 30))
                .setSuccessor(new CornerSpaceDispenser(commandBuilderDispatcher, 0, 10, 20))
                .setSuccessor(new PropertySpaceDispenser(commandBuilderDispatcher, properties));
        for (int i = 0; i < 40; i++) {
            spaces.add(chainOfResponsability.processSpace(i));
        }
    }

    public List<Space> getSpaces() {
        return spaces;
    }

    public Space getSpace(int position) {
        if (position > 0 && position < spaces.size()) {
            return spaces.get(position);
        }
        return null;
    }
}
