package cat.itacademy.s05.t01.model.participant;

import cat.itacademy.s05.t01.model.enums.ParticipantFinalStatus;
import cat.itacademy.s05.t01.model.card.AceCard;
import cat.itacademy.s05.t01.model.card.Card;
import cat.itacademy.s05.t01.model.card.NonAceCard;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class GameParticipant {
    private int handValue;
    private List<Card> cardsInHand = new ArrayList<>();
    private boolean isActive = true;
    private ParticipantFinalStatus finalStatus;

    public void addCard(Card givenCard) {
        cardsInHand.add(givenCard);
        recalculateHandValue();
        if (handValue > 21) bust();
    }

    private void recalculateHandValue() {
        int nonAceCardsValue = cardsInHand.stream()
                .filter(card -> card instanceof NonAceCard)
                .mapToInt(card -> ((NonAceCard) card).getValue())
                .sum();
        int numberAceCards = (int) cardsInHand.stream()
                .filter(card -> card instanceof AceCard).count();

        if (numberAceCards == 0) handValue = nonAceCardsValue;
        else {
            int totalCardsValue = nonAceCardsValue;
            for (int i = 0; i < numberAceCards; i++) {
                if (totalCardsValue + 11 > 21) totalCardsValue++;
                else totalCardsValue = totalCardsValue + 11;
            }
            handValue = totalCardsValue;
        }
    }

    private void bust() {
        isActive = false;
        finalStatus = ParticipantFinalStatus.LOSE;
    }

    public void stand() {
        isActive = false;
    }
}
