package cat.itacademy.s05.t01.service;

import cat.itacademy.s05.t01.model.Game;
import cat.itacademy.s05.t01.model.User;
import cat.itacademy.s05.t01.model.dto.UserWithRowNumber;
import reactor.core.publisher.Flux;

public interface UserService {
    Flux<User> insertScores(Game game);
    Flux<UserWithRowNumber> getAllUsersSortedByScore();
}
