
package com.bol.mancala.game.model;

import lombok.Getter;

@Getter
public enum PlayerTurns {
    PlayerA ("A"), PlayerB ("B");

    private String turn;

    PlayerTurns(String turn) {
        this.turn = turn;
    }

    @Override
    public String toString() {
        return turn;
    }
}
