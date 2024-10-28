package cat.itacademy.s05.t01.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MoveResponseDTO {
    private String info;
    private String playerName;
    private String move;
    private int handValue;
    private boolean turnContinues;
}

