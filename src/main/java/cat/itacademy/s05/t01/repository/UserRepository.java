package cat.itacademy.s05.t01.repository;

import cat.itacademy.s05.t01.model.User;
import cat.itacademy.s05.t01.model.dto.UserWithRowNumber;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends R2dbcRepository<User, Integer> {
    Mono<User> findByName(String name);

    @Query("SELECT id, ROW_NUMBER() OVER () AS \"row_number\", name, score FROM blackjackdb.users ORDER BY score DESC;")
    Flux<UserWithRowNumber> findAllByScoreDesc();
}
