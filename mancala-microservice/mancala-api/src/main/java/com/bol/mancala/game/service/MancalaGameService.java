/**
 * Copyright 2019 Esfandiyar Talebi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bol.mancala.game.service;

import com.bol.mancala.game.interfaces.MancalaGame;
import com.bol.mancala.game.exceptions.ResourceNotFoundException;
import com.bol.mancala.game.repository.MancalaGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*
    This class implements data persistence and caching for Kala Games objects
 */

@Service
public class MancalaGameService implements MancalaGame {

    @Autowired
    private MancalaGameRepository MancalaGameRepository;

    @Override
    public com.bol.mancala.game.model.MancalaGame createGame(int pitStones) {

        com.bol.mancala.game.model.MancalaGame MancalaGame = new com.bol.mancala.game.model.MancalaGame(pitStones);

        MancalaGameRepository.save(MancalaGame);

        return MancalaGame;
    }

    // loads the game instance from the Cache if game instance was found
    @Cacheable (value = "kalahGames", key = "#id" , unless = "#result  == null")
    public com.bol.mancala.game.model.MancalaGame loadGame (String id) throws ResourceNotFoundException {
        Optional<com.bol.mancala.game.model.MancalaGame> gameOptional = MancalaGameRepository.findById(id);

        if (!gameOptional.isPresent())
            throw new ResourceNotFoundException("Game id " + id + " not found!");

        return gameOptional.get();
    }

    // put the updated game instance into cache as well as data store
    @CachePut(value = "kalahGames", key = "#MancalaGame.id")
    public com.bol.mancala.game.model.MancalaGame updateGame (com.bol.mancala.game.model.MancalaGame MancalaGame){
        MancalaGame = MancalaGameRepository.save(MancalaGame);
        return MancalaGame;
    }
}
