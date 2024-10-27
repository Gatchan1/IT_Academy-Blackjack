package cat.itacademy.s05.t01.service;

import cat.itacademy.s05.t01.model.Game;
import cat.itacademy.s05.t01.model.enums.ParticipantAction;
import reactor.core.publisher.Mono;

public interface GameService {
    Mono<Game> createGame(Game newGame);
    Mono<Game> getGameDetails(String id);
    Mono<String> makeMove(String id, ParticipantAction participantAction);
    Mono<Game> deleteGame(String id);
}
