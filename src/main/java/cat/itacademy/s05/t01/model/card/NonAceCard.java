package cat.itacademy.s05.t01.model.card;

import lombok.Data;

@Data
public class NonAceCard implements Card{
    private int value;

    public NonAceCard(int value) {
        this.value = value;
    }
}
