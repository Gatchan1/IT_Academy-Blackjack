package cat.itacademy.s05.t01.model.participant;

import lombok.Data;

@Data
public class Player extends GameParticipant {
    private String name;
    private int initialBet;
}
