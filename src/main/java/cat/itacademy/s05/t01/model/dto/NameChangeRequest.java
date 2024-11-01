package cat.itacademy.s05.t01.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NameChangeRequest {
    @NotNull(message = "New name cannot be null")
    private String newName;
}
