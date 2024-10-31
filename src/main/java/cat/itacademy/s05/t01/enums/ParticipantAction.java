package cat.itacademy.s05.t01.enums;

import cat.itacademy.s05.t01.exception.custom.InvalidParticipantActionException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;

@JsonDeserialize(using = ParticipantActionDeserializer.class)
public enum ParticipantAction {
    HIT,
    STAND;
}

class ParticipantActionDeserializer extends JsonDeserializer<ParticipantAction> {
    @Override
    public ParticipantAction deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();
        try {
            return ParticipantAction.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidParticipantActionException("Invalid action: " + value);
        }
    }
}