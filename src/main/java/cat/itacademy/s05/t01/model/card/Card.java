package cat.itacademy.s05.t01.model.card;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AceCard.class, name = "AceCard"),
        @JsonSubTypes.Type(value = NonAceCard.class, name = "NonAceCard")
})
public interface Card {
}
