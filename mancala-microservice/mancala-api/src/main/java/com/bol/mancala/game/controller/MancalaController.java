package com.bol.mancala.game.controller;

import com.bol.mancala.game.constants.MancalaGameConstants;
import com.bol.mancala.game.exceptions.MancalaApiException;
import com.bol.mancala.game.model.MancalaGame;
import com.bol.mancala.game.service.MancalaGameService;
import com.bol.mancala.game.service.MancalaSowingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/games")
@Api(value = "Mancala game API. Set of endpoints for Creating and Sowing the Game")
public class MancalaController {

    @Autowired
    private MancalaGameService mancalaGameService;

    @Autowired
    private MancalaSowingService mancalaSowingService;

    @Value("${mancala.pit.stones}")
    private Integer pitStones;

    @PostMapping
    @ApiOperation(value = "Endpoint for creating new Mancala game instance. It returns a MancalaGame object with unique GameId used for sowing the game",
            produces = "Application/JSON", response = MancalaGame.class, httpMethod = "POST")
    public ResponseEntity<MancalaGame> createGame() throws Exception {

        log.info("Invoking create() endpoint... ");

        MancalaGame game = mancalaGameService.createGame(pitStones);

        log.info("Game instance created. Id=" + game.getId());

        log.info(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(game));

        mancalaGameService.updateGame(game);

        return ResponseEntity.ok(game);
    }

    @PutMapping(value = "{gameId}/pits/{pitId}")
    @ApiOperation(value = "Endpoint for sowing the game. It keeps the history of the Game instance for consecutive requests. ",
            produces = "Application/JSON", response = MancalaGame.class, httpMethod = "PUT")
    public ResponseEntity<MancalaGame> sowGame(
            @ApiParam(value = "The id of game created by calling createGame() method. It can't be empty or null", required = true)
            @PathVariable(value = "gameId") String gameId,
            @PathVariable(value = "pitId") Integer pitId) throws Exception {

        log.info("Invoking sow() endpoint. GameId: " + gameId + "  , pit Index: " + pitId);

        if (pitId == null || pitId < 1 || pitId >= MancalaGameConstants.leftPitHouseId || pitId == MancalaGameConstants.rightPitHouseId)
            throw new MancalaApiException("Invalid pit Index!. It should be between 1..6 or 8..13");

        MancalaGame MancalaGame = mancalaGameService.loadGame(gameId);

        MancalaGame = mancalaSowingService.sow(MancalaGame, pitId);

        mancalaGameService.updateGame(MancalaGame);

        log.info("sow is called for Game id:" + gameId + " , pitIndex:" + pitId);

        log.info(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(MancalaGame));

        return ResponseEntity.ok(MancalaGame);
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Endpoint for returning the latest status of the Game",
            produces = "Application/JSON", response = MancalaGame.class, httpMethod = "GET")
    public ResponseEntity<MancalaGame> gameStatus(
            @ApiParam(value = "The id of game created by calling createGame() method. It's an String e.g. 5d34968590fcbd35b086bc21. It can't be empty or null",
                    required = true)
            @PathVariable(value = "id") String gameId) throws Exception {

        return ResponseEntity.ok(mancalaGameService.loadGame(gameId));
    }


}
