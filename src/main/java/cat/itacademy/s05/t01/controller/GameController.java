package cat.itacademy.s05.t01.controller;

import cat.itacademy.s05.t01.model.Game;
import cat.itacademy.s05.t01.model.enums.ParticipantAction;
import cat.itacademy.s05.t01.service.impl.GameServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/game")
public class GameController {
    @Autowired
    private GameServiceImpl gameService;

    //post "/game/new"
    @PostMapping("/new")
    public Mono<ResponseEntity<Game>> createGame(@RequestBody Game newGame) {
        return gameService.createGame(newGame)
                .map(createdGame -> ResponseEntity
                        .created(URI.create("/game/" + createdGame.getId()))
                        .body(createdGame))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    // start new game. body -> player names & initial bets
    // successful response = Code 201 Created + info on the game.

    //get "/game/{id}"
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Game>> getGameDetails(@PathVariable String id) {
        return gameService.getGameDetails(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));   //linea redundante?
    }
    // get game details:
    // Whose turn is it, what total cards value does everyone have...
    // successful response = Code 200 OK + the game details

    //post "game/{id}/play"
    @PostMapping("/{id}/play")
    public Mono<ResponseEntity<String>> makeMove(@PathVariable String id, @RequestBody ParticipantAction participantAction) {
        return gameService.makeMove(id, participantAction)
                .map(moveResult -> ResponseEntity
                        .ok()
                        .body(moveResult))
                        .switchIfEmpty(Mono.just(ResponseEntity.notFound().build())); //linea redundante?
    }
    // a player makes a turn (body -> hit or stand)
    // successful response = Code 200 OK +:
    // if the play was a "hit", what card did the player get
    // and what's his total cards value.
    // Also: is the game running or over.

    //delete "game/{id}/delete"
    // delete one specific game
    // successful response = Code 204 No Content
}
