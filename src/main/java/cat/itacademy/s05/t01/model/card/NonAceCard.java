package cat.itacademy.s05.t01.model.card;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NonAceCard implements Card{
    private int value;

    public NonAceCard(int value) {
        this.value = value;
    }
}
