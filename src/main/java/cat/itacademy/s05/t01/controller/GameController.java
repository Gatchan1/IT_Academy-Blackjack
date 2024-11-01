package cat.itacademy.s05.t01.controller;

import cat.itacademy.s05.t01.model.Game;
import cat.itacademy.s05.t01.enums.ParticipantAction;
import cat.itacademy.s05.t01.model.dto.MoveResponse;
import cat.itacademy.s05.t01.service.impl.GameServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game")
public class GameController {
    private final GameServiceImpl gameService;

    //post "/game/new"
    @PostMapping("/new")
    public Mono<ResponseEntity<Game>> createGame(@RequestBody @Valid Game newGame) {
        return gameService.createGame(newGame)
                .map(createdGame -> ResponseEntity
                        .created(URI.create("/game/" + createdGame.getId()))
                        .body(createdGame))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    //get "/game/{id}"
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Game>> getGameDetails(@PathVariable String id) {
        return gameService.getGameDetails(id)
                .map(ResponseEntity::ok);
    }

    //post "game/{id}/play"
    @PostMapping("/{id}/play")
    public Mono<ResponseEntity<MoveResponse>> makeMove(@PathVariable String id, @RequestBody ParticipantAction participantAction) {
        return gameService.makeMove(id, participantAction)
                .map(moveResponse -> ResponseEntity
                        .ok()
                        .body(moveResponse));
    }

    //delete "game/{id}/delete"
    @DeleteMapping("/{id}/delete")
    public Mono<ResponseEntity<Void>> deleteGame(@PathVariable String id) {
        return gameService.deleteGame(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
