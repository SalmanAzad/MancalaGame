
package com.bol.mancala.game.model;

import com.bol.mancala.game.exceptions.MancalaApiException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static com.bol.mancala.game.constants.MancalaGameConstants.*;

@Document(collection = "games")
@Setter
@Getter
public class MancalaGame implements Serializable{

    @Id
    private String id;

    private List<MancalaPit> pits;

    private PlayerTurns playerTurn;

    @JsonIgnore
    private int currentPitIndex;

    public MancalaGame() {
        this (defaultPitStones);
    }

    public MancalaGame(int pitStones) {
        this.pits = Arrays.asList(
                new MancalaPit(firstPitPlayerA, pitStones),
                new MancalaPit(secondPitPlayerA, pitStones),
                new MancalaPit(thirdPitPlayerA, pitStones),
                new MancalaPit(forthPitPlayerA, pitStones),
                new MancalaPit(fifthPitPlayerA, pitStones),
                new MancalaPit(sixthPitPlayerA, pitStones),
                new MancalaHouse(rightPitHouseId),
                new MancalaPit(firstPitPlayerB, pitStones),
                new MancalaPit(secondPitPlayerB, pitStones),
                new MancalaPit(thirdPitPlayerB, pitStones),
                new MancalaPit(forthPitPlayerB, pitStones),
                new MancalaPit(fifthPitPlayerB, pitStones),
                new MancalaPit(sixthPitPlayerB, pitStones),
                new MancalaHouse(leftPitHouseId));
    }

    public MancalaGame(String id, Integer pitStones) {
        this (pitStones);
        this.id = id;
    }

    // returns the corresponding pit of particular index
    public MancalaPit getPit(Integer pitIndex) throws MancalaApiException {
        try {
            return this.pits.get(pitIndex-1);
        }catch (Exception e){
            throw  new MancalaApiException("Invalid pitIndex:"+ pitIndex +" has given!");
        }
    }

    @Override
    public String toString() {
        return "MancalaGame{" +
                ", pits=" + pits +
                ", playerTurn=" + playerTurn +
                '}';
    }
}
