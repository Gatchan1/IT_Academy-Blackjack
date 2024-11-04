package cat.itacademy.s05.t01.controller;

import cat.itacademy.s05.t01.model.Game;
import cat.itacademy.s05.t01.enums.ParticipantAction;
import cat.itacademy.s05.t01.model.dto.MoveResponse;
import cat.itacademy.s05.t01.model.dto.SimplePlayer;
import cat.itacademy.s05.t01.service.impl.GameServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game")
@Tag(name = "Game API Service", description = "Operations related to Game API Service (MongoDB)")
public class GameController {
    private final GameServiceImpl gameService;

    //post "/game/new"
    @Operation(
            description = "Create New Game",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Names and initial bets for the players",
                    required = true)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Game created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content)
    })
    @PostMapping("/new")
    public Mono<ResponseEntity<Game>> createGame(@RequestBody List<@Valid SimplePlayer> players) {
                return gameService.createGame(players)
                .map(createdGame -> ResponseEntity
                        .created(URI.create("/game/" + createdGame.getId()))
                        .body(createdGame))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    //get "/game/{id}"
    @Operation(description = "Retrieve game details by game ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game successfully found"),
            @ApiResponse(responseCode = "404", description = "There's no game with such id", content = @Content)
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Game>> getGameDetails(@Parameter(name = "id",
            description = "Unique identifier of the game") @PathVariable String id) {
        return gameService.getGameDetails(id)
                .map(ResponseEntity::ok);
    }

    //post "game/{id}/play"
    @Operation(description = "Make a move for the player whose turn we're in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Move successfully done"),
            @ApiResponse(responseCode = "400", description = "Invalid request body. Only supports \"hit\" or \"stand\"",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "There's no game with such id", content = @Content)
    })
    @PostMapping("/{id}/play")
    public Mono<ResponseEntity<MoveResponse>> makeMove(@Parameter(name = "id",
            description = "Unique identifier of the game") @PathVariable String id,
            @RequestBody ParticipantAction participantAction) {
        return gameService.makeMove(id, participantAction)
                .map(moveResponse -> ResponseEntity
                        .ok()
                        .body(moveResponse));
    }

    //delete "game/{id}/delete"
    @Operation(description = "Delete a game by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Game successfully deleted"),
            @ApiResponse(responseCode = "404", description = "There's no game with such id", content = @Content)
    })
    @DeleteMapping("/{id}/delete")
    public Mono<ResponseEntity<Void>> deleteGame(@Parameter(name = "id",
            description = "Unique identifier of the game") @PathVariable String id) {
        return gameService.deleteGame(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
