package cat.itacademy.s05.t01.model.participant;

import cat.itacademy.s05.t01.model.enums.ParticipantFinalStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player extends GameParticipant {
    private String name;
    private int initialBet;
}
