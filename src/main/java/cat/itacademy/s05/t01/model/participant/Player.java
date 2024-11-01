package cat.itacademy.s05.t01.model.participant;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Player extends GameParticipant {
    @NotNull(message = "Player name cannot be null")
    private String name;
    private int initialBet;
}
