package cat.itacademy.s05.t01.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@NoArgsConstructor
public class User {
    @Id private Long id;
    @Setter private String name;
    @Setter private int score;

    public User(String name, int score) {
        this.name = name;
        this.score = score;
    }

    //method winGame? (add 10 points to score)
    //method loseGame? (subtract 10 points to score)
}
