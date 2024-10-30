package cat.itacademy.s05.t01.service.impl;

import cat.itacademy.s05.t01.enums.PlayerFinalStatus;
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
}
