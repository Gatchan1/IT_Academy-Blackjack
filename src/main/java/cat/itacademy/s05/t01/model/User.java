package cat.itacademy.s05.t01.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@NoArgsConstructor
@Table("users")
public class User {
    @Id private Long id;
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
