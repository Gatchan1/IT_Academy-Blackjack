package cat.itacademy.s05.t01.model.participant;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player extends GameParticipant {
    @NotNull(message = "Player name cannot be null")
    private String name;

    private int initialBet;
}
