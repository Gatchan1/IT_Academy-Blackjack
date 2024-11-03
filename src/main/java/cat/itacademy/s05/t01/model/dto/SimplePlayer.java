package cat.itacademy.s05.t01.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimplePlayer {
    @NotEmpty(message = "Player name cannot be null")
    private String name;

    private int initialBet;
}
