package cat.itacademy.s05.t01.model.dto;

import lombok.Data;

@Data
public class UserWithRowNumber {
    private int id;
    private int rowNumber;
    private String name;
    private int score;
}
