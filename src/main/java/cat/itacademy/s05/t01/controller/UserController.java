package cat.itacademy.s05.t01.controller;

import cat.itacademy.s05.t01.model.User;
import cat.itacademy.s05.t01.model.dto.NameChangeRequest;
import cat.itacademy.s05.t01.model.dto.UserWithRowNumber;
import cat.itacademy.s05.t01.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    
    //get "/ranking"
    @GetMapping("/ranking")
    public Mono<ResponseEntity<Flux<UserWithRowNumber>>> getRanking() {
        return Mono.just(ResponseEntity.ok(userService.getAllUsersSortedByScore()));
    }

    //put "/player/{playerId}"
    @PutMapping("/player/{playerId}")
    public Mono<ResponseEntity<User>> changeUserName(@PathVariable int playerId, @RequestBody @Valid NameChangeRequest request) {
        String newName = request.getNewName();
        return userService.changeUserName(playerId, newName)
                .map(ResponseEntity::ok);
    }
}
