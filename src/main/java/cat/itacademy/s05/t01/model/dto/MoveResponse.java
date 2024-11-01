package cat.itacademy.s05.t01.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class MoveResponse {
    private String info;
    private String playerName;
    private String move;
    private int handValue;
    private boolean turnContinues;
    private boolean gameOver;
}

