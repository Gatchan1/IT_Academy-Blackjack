package cat.itacademy.s05.t01.service.impl;

import cat.itacademy.s05.t01.enums.PlayerFinalStatus;
import cat.itacademy.s05.t01.exception.custom.UserNotFoundException;
import cat.itacademy.s05.t01.model.Game;
import cat.itacademy.s05.t01.model.User;
import cat.itacademy.s05.t01.model.dto.UserWithRowNumber;
import cat.itacademy.s05.t01.model.participant.Player;
import cat.itacademy.s05.t01.repository.UserRepository;
import cat.itacademy.s05.t01.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Flux<User> insertScores(Game game) {
        return Flux.fromIterable(game.getPlayers())
                .flatMap(player -> {
                    String name = player.getName();
                    return userRepository.findByName(name)
                            .flatMap(existingUser -> {
                                assignPoints(player, existingUser);
                                return userRepository.save(existingUser);
                            })
                            .switchIfEmpty(Mono.defer(() -> {
                                User newUser = new User(name);
                                assignPoints(player, newUser);
                                return userRepository.save(newUser);
                            }));
                });
    }

    private void assignPoints(Player player, User user) {
        if (player.getFinalStatus() == PlayerFinalStatus.WIN) user.winGame();
        if (player.getFinalStatus() == PlayerFinalStatus.LOSE) user.loseGame();
    }

    @Override
    public Flux<UserWithRowNumber> getAllUsersSortedByScore() {
        return userRepository.findAllByScoreDesc();
    }

    @Override
    public Mono<User> changeUserName(Integer playerId, String newName) {
        return userRepository.findById(playerId)
                .switchIfEmpty(Mono.error(new UserNotFoundException("Player not found with id " + playerId)))
                .flatMap(changingUser -> {
                    if (changingUser.getName().equals(newName)) return Mono.just(changingUser);
                    return userRepository.findByName(newName)
                            .flatMap(existingUser -> {
                                existingUser.setScore(existingUser.getScore() + changingUser.getScore());
                                return userRepository.save(existingUser)
                                        .then(userRepository.delete(changingUser).thenReturn(existingUser));
                            })
                            .switchIfEmpty(Mono.defer(() -> {
                                changingUser.setName(newName);
                                return userRepository.save(changingUser);
                            }));
                });
    }
}