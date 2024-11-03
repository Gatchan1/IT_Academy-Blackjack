package cat.itacademy.s05.t01.service;

import cat.itacademy.s05.t01.model.Game;
import cat.itacademy.s05.t01.enums.ParticipantAction;
import cat.itacademy.s05.t01.model.dto.MoveResponse;
import cat.itacademy.s05.t01.model.dto.SimplePlayer;
import reactor.core.publisher.Mono;

import java.util.List;

public interface GameService {
    Mono<Game> createGame(List<SimplePlayer> players);
    Mono<Game> getGameDetails(String id);
    Mono<MoveResponse> makeMove(String id, ParticipantAction participantAction);
    Mono<Void> deleteGame(String id);
}
