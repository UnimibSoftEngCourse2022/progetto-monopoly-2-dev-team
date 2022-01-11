package controller.board;

import controller.board.space.Space;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final List<Space> gameboard;

    public Board() {
        this.gameboard = new ArrayList<>();
    }
}
