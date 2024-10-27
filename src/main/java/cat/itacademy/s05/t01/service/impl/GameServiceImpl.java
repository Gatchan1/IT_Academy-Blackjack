package cat.itacademy.s05.t01.service.impl;

import cat.itacademy.s05.t01.exception.InactiveGameException;
import cat.itacademy.s05.t01.exception.NoGameFoundException;
import cat.itacademy.s05.t01.model.Game;
import cat.itacademy.s05.t01.model.enums.ParticipantAction;
import cat.itacademy.s05.t01.repository.GameRepository;
import cat.itacademy.s05.t01.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GameServiceImpl implements GameService {
    @Autowired
    private GameRepository gameRepository;

    @Override
    public Mono<Game> createGame(Game newGame) {
        newGame.dealInitialCards();
        return gameRepository.save(newGame);
    }

    @Override
    public Mono<Game> getGameDetails(String id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new NoGameFoundException("Game not found with id " + id)));
    }
    //el switchIfEmpty no sé si hace falta? ya que se gestiona bien el null desde el controller...!

    @Override
    public Mono<String> makeMove(String id, ParticipantAction participantAction) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new NoGameFoundException("Game not found with id " + id)))
                .flatMap(existingGame -> {
                    if (!existingGame.isActive()) {
                        return Mono.error(new InactiveGameException("Game with id " + id + " is over."));
                    }
                    String result = existingGame.makeMove(participantAction);

                    return gameRepository.save(existingGame).thenReturn(result);
                });
    }


    @Override
    public Mono<Game> deleteGame(String id) {
        return null;
    }
}
