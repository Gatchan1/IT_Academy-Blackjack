package cat.itacademy.s05.t01.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("users")
public class User {
    @Id private int id;
    @Setter private String name;
    @Setter private int score;

    public User(String name) {
        this.name = name;
    }

    public void winGame() {
        score += 10;
    }

    public void loseGame() {
        score -= 10;
    }
}
