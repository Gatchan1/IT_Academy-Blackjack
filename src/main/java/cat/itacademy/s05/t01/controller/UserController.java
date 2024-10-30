package cat.itacademy.s05.t01.controller;

import cat.itacademy.s05.t01.model.User;
import cat.itacademy.s05.t01.model.dto.UserWithRowNumber;
import cat.itacademy.s05.t01.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    //(mysql)
    
    //get "/ranking"            (muestra ranking)
    @GetMapping("/ranking")
    public Mono<ResponseEntity<Flux<UserWithRowNumber>>> getRanking() {
        return Mono.just(ResponseEntity.ok(userService.getAllUsersSortedByScore()));
    }
    // Resposta exitosa: Codi 200 OK amb la llista de jugadors ...

    //put "/player/{playerId}"  (cambiar nombre del jugador)
    // Resposta exitosa: Codi 200 OK amb informació actualitzada del jugador.
}
