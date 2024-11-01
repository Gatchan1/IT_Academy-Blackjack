package cat.itacademy.s05.t01.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

@Configuration
public class MySQLDatabaseConfig {
    @Autowired
    private DatabaseClient databaseClient;

    @Bean
    public Mono<Void> initializeDatabase() {
        return databaseClient.sql("""
                CREATE TABLE IF NOT EXISTS users (
                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(100) NOT NULL,
                    score INT NOT NULL DEFAULT 0
                )
                """)
                .then();
    }
}
