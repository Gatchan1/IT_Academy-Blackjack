package cat.itacademy.s05.t01.service.impl;

import cat.itacademy.s05.t01.exception.custom.InactiveGameException;
import cat.itacademy.s05.t01.exception.custom.GameNotFoundException;
import cat.itacademy.s05.t01.model.Game;
import cat.itacademy.s05.t01.enums.ParticipantAction;
import cat.itacademy.s05.t01.model.dto.MoveResponse;
import cat.itacademy.s05.t01.repository.GameRepository;
import cat.itacademy.s05.t01.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final UserServiceImpl userService;

    @Override
    public Mono<Game> createGame(Game newGame) {
        newGame.dealInitialCards();
        return gameRepository.save(newGame);
    }

    @Override
    public Mono<Game> getGameDetails(String id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game not found with id " + id)));
    }

    @Override
    public Mono<MoveResponse> makeMove(String id, ParticipantAction participantAction) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game not found with id " + id)))
                .flatMap(existingGame -> {
                    if (!existingGame.isActive()) {
                        return Mono.error(new InactiveGameException("Game with id " + id + " is over, " +
                                "doesn't allow new moves."));
                    }
                    MoveResponse result = existingGame.makeMove(participantAction);
                    Mono<Game> saveGameMono = gameRepository.save(existingGame);

                    if (result.isGameOver()) {
                        return userService.insertScores(existingGame)
                                .thenMany(saveGameMono)
                                .then(Mono.just(result));
                    } else {
                        return saveGameMono.thenReturn(result);
                    }
                });
    }


    @Override
    public Mono<Void> deleteGame(String id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game not found with ID " + id)))
                .flatMap(existingGame -> gameRepository.deleteById(id));
    }
}
