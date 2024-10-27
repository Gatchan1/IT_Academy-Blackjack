package cat.itacademy.s05.t01.repository;

import cat.itacademy.s05.t01.model.Game;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface GameRepository extends ReactiveMongoRepository<Game, String> {
}
