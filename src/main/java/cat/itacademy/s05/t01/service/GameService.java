package cat.itacademy.s05.t01.service;

import cat.itacademy.s05.t01.model.Game;
import cat.itacademy.s05.t01.enums.ParticipantAction;
import cat.itacademy.s05.t01.model.dto.MoveResponse;
import reactor.core.publisher.Mono;

public interface GameService {
    Mono<Game> createGame(Game newGame);
    Mono<Game> getGameDetails(String id);
    Mono<MoveResponse> makeMove(String id, ParticipantAction participantAction);
    Mono<Void> deleteGame(String id);
}
