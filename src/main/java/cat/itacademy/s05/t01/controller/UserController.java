package cat.itacademy.s05.t01.controller;

import cat.itacademy.s05.t01.model.User;
import cat.itacademy.s05.t01.model.dto.NameChangeRequest;
import cat.itacademy.s05.t01.model.dto.UserWithRowNumber;
import cat.itacademy.s05.t01.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Tag(name = "User API Service", description = "Operations related to User API Service (MySQL)")
public class UserController {
    private final UserServiceImpl userService;
    
    //get "/ranking"
    @Operation(description = "Show ranking of players")
    @ApiResponse(responseCode = "200", description = "Ranking successfully shown")
    @GetMapping("/ranking")
    public Mono<ResponseEntity<Flux<UserWithRowNumber>>> getRanking() {
        return Mono.just(ResponseEntity.ok(userService.getAllUsersSortedByScore()));
    }

    //put "/player/{playerId}"
    @Operation(description = "Change name of player")
    @ApiResponse(responseCode = "200", description = "Name successfully changed")
    @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content)
    @ApiResponse(responseCode = "404", description = "There's no player with such id", content = @Content)
    @PutMapping("/player/{playerId}")
    public Mono<ResponseEntity<User>> changeUserName(@Parameter(name = "playerId",
            description = "Unique identifier of the player") @PathVariable int playerId,
            @RequestBody @Valid NameChangeRequest request) {
        String newName = request.getNewName();
        return userService.changeUserName(playerId, newName)
                .map(ResponseEntity::ok);
    }
}
