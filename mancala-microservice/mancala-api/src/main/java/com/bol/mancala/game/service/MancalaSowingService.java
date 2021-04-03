package com.bol.mancala.game.service;

import com.bol.mancala.game.interfaces.MancalaSowing;
import com.bol.mancala.game.constants.MancalaGameConstants;
import com.bol.mancala.game.model.MancalaGame;
import com.bol.mancala.game.model.MancalaPit;
import com.bol.mancala.game.model.PlayerTurns;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
public class MancalaSowingService implements MancalaSowing {

    // This method perform sowing the game on specific pit index
    @Override
    public MancalaGame sow(MancalaGame game, int requestedPitId) {

        // No movement on House pits
        if (requestedPitId == MancalaGameConstants.rightPitHouseId || requestedPitId == MancalaGameConstants.leftPitHouseId)
            return game;

        // we set the player turn for the first move of the game based on the pit id
        if (game.getPlayerTurn() == null) {
            if (requestedPitId < MancalaGameConstants.rightPitHouseId)
                game.setPlayerTurn(PlayerTurns.PlayerA);
            else
                game.setPlayerTurn(PlayerTurns.PlayerB);
        }

        // we need to check if request comes from the right player otherwise we do not sow the game. In other words,
        // we keep the turn for the correct player
        if (game.getPlayerTurn() == PlayerTurns.PlayerA && requestedPitId > MancalaGameConstants.rightPitHouseId ||
                game.getPlayerTurn() == PlayerTurns.PlayerB && requestedPitId < MancalaGameConstants.rightPitHouseId)
            return game;

        MancalaPit selectedPit = game.getPit(requestedPitId);

        int stones = selectedPit.getStones();

        // No movement for empty Pits
        if (stones == MancalaGameConstants.emptyStone)
            return game;

        selectedPit.setStones(MancalaGameConstants.emptyStone);

        // keep the pit index, used for sowing the stones in right pits
        game.setCurrentPitIndex(requestedPitId);

        // simply sow all stones except the last one
        IntStream.range(0, stones -1)
                .forEach(index-> sowRight(game, false));

        // converted to Java8 api above
        /*for (int i = 0; i < stones - 1; i++) {
            sowRight(game,false);
        }*/

        // simply the last stone
        sowRight(game,true);

        int currentPitIndex = game.getCurrentPitIndex();

        // we switch the turn if the last sow was not on any of pit houses (left or right)
        if (currentPitIndex != MancalaGameConstants. rightPitHouseId && currentPitIndex != MancalaGameConstants.leftPitHouseId)
            game.setPlayerTurn(nextTurn(game.getPlayerTurn()));

        return game;
    }

    // sow the game one pit to the right
    private void sowRight(MancalaGame game, Boolean lastStone) {
        int currentPitIndex = game.getCurrentPitIndex() % MancalaGameConstants.totalPits + 1;

        PlayerTurns playerTurn = game.getPlayerTurn();

        if ((currentPitIndex == MancalaGameConstants.rightPitHouseId && playerTurn == PlayerTurns.PlayerB) ||
                (currentPitIndex == MancalaGameConstants.leftPitHouseId && playerTurn == PlayerTurns.PlayerA))
            currentPitIndex = currentPitIndex % MancalaGameConstants.totalPits + 1;

        game.setCurrentPitIndex(currentPitIndex);

        MancalaPit targetPit = game.getPit(currentPitIndex);
        if (!lastStone || currentPitIndex == MancalaGameConstants.rightPitHouseId || currentPitIndex == MancalaGameConstants.leftPitHouseId) {
            targetPit.sow();
            return;
        }

        // It's the last stone and we need to check the opposite player's pit status
        MancalaPit oppositePit = game.getPit(MancalaGameConstants.totalPits - currentPitIndex);

        // we are sowing the last stone and the current player's pit is empty but the opposite pit is not empty, therefore,
        // we collect the opposite's Pit stones plus the last stone and add them to the House Pit of current player and
        // make the opposite Pit empty
        if (targetPit.isEmpty() && !oppositePit.isEmpty()) {
            Integer oppositeStones = oppositePit.getStones();
            oppositePit.clear();
            Integer pitHouseIndex = currentPitIndex < MancalaGameConstants.rightPitHouseId ? MancalaGameConstants.rightPitHouseId : MancalaGameConstants.leftPitHouseId;
            MancalaPit pitHouse = game.getPit(pitHouseIndex);
            pitHouse.addStones(oppositeStones + 1);
            return;
        }

        targetPit.sow();
    }

    public PlayerTurns nextTurn(PlayerTurns currentTurn) {
        if (currentTurn == PlayerTurns.PlayerA)
            return PlayerTurns.PlayerB;
        return PlayerTurns.PlayerA;
    }
}
