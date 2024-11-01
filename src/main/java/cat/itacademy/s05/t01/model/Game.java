package cat.itacademy.s05.t01.model;

import cat.itacademy.s05.t01.enums.PlayerFinalStatus;
import cat.itacademy.s05.t01.model.card.AceCard;
import cat.itacademy.s05.t01.model.card.Card;
import cat.itacademy.s05.t01.model.card.NonAceCard;
import cat.itacademy.s05.t01.enums.ParticipantAction;
import cat.itacademy.s05.t01.model.dto.MoveResponse;
import cat.itacademy.s05.t01.model.participant.Croupier;
import cat.itacademy.s05.t01.model.participant.GameParticipant;
import cat.itacademy.s05.t01.model.participant.Player;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class Game {
    @Id private String id;
    @Setter private boolean isActive = true;
    @Setter private int playerTurn;
    @Setter private List<? extends Card> undealtCards = generateInitialCardList();
    @Setter private Croupier croupier = new Croupier();
    @Setter private List<Player> players;

    private List<? extends Card> generateInitialCardList() {
        return Stream.of(
                        Collections.nCopies(16, new NonAceCard(10)),
                        Collections.nCopies(4, new AceCard()),
                        Collections.nCopies(4, new NonAceCard(2)),
                        Collections.nCopies(4, new NonAceCard(3)),
                        Collections.nCopies(4, new NonAceCard(4)),
                        Collections.nCopies(4, new NonAceCard(5)),
                        Collections.nCopies(4, new NonAceCard(6)),
                        Collections.nCopies(4, new NonAceCard(7)),
                        Collections.nCopies(4, new NonAceCard(8)),
                        Collections.nCopies(4, new NonAceCard(9))
                )
                .flatMap(List::stream)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Player getCurrentPlayer() { //if this was public this "attribute" would be serialized!
        return players.get(playerTurn);
    }

    public void dealInitialCards() {
        giveCardToParticipant(croupier, retrieveCardFromDrawPile());
        for (Player player : players) {
            giveCardToParticipant(player, retrieveCardFromDrawPile());
            giveCardToParticipant(player, retrieveCardFromDrawPile());
        }
    }

    private Card retrieveCardFromDrawPile() {
        Random random = new Random();
        int randomIndex = random.nextInt(undealtCards.size());
        return undealtCards.remove(randomIndex);
    }

    private void giveCardToParticipant(GameParticipant participant, Card card) {
        participant.addCard(card);
    }

    private void dealCardToCurrentPlayer() {
        giveCardToParticipant(getCurrentPlayer(), retrieveCardFromDrawPile());
        if (!getCurrentPlayer().isActive()) playerTurn++;
    }

    private boolean isLastPlayerTurnOver() {
        return playerTurn >= players.size();
    }

    public MoveResponse makeMove(ParticipantAction participantAction) {
        Player playerThatMakesMove = getCurrentPlayer();
        if (participantAction == ParticipantAction.HIT) {
            dealCardToCurrentPlayer();
        } else if (participantAction == ParticipantAction.STAND) {
            playerThatMakesMove.stand();
            playerTurn++;
        }
        if (isLastPlayerTurnOver()) {
            while (croupier.isActive()) {
                if (croupier.getHandValue() < 17) {
                    giveCardToParticipant(croupier, retrieveCardFromDrawPile());
                } else croupier.stand();
            }
            isActive = false;
            determinePlayersFinalStatuses();
        }
        return generateMoveResponseDTO(playerThatMakesMove, participantAction);
    }

    private void determinePlayersFinalStatuses() {
        players.forEach(player -> player.setFinalStatus(determinePlayerFinalStatus(
                player.getHandValue(), croupier.getHandValue())));
    }

    private PlayerFinalStatus determinePlayerFinalStatus(int playerHandValue, int croupierHandValue) {
        boolean playerBusted = playerHandValue > 21;
        boolean croupierBusted = croupierHandValue > 21;

        if (playerBusted) return PlayerFinalStatus.LOSE;
        if (!croupierBusted && playerHandValue < croupierHandValue) return PlayerFinalStatus.LOSE;
        if (!croupierBusted && playerHandValue == croupierHandValue) return PlayerFinalStatus.TIE;

        return PlayerFinalStatus.WIN;
    }

    private MoveResponse generateMoveResponseDTO(Player player, ParticipantAction action) {
        return MoveResponse.builder()
                .info(getMoveInfo(player, action))
                .playerName(player.getName())
                .move(action.name())
                .handValue(player.getHandValue())
                .turnContinues(player.isActive())
                .gameOver(!isActive)
                .build();
    }

    private String getMoveInfo(Player player, ParticipantAction participantAction) {
        StringBuilder builder = new StringBuilder()
                .append("%s has made a \"%s\" move.\n"
                        .formatted(player.getName(), participantAction.name().toLowerCase()));

        if (participantAction == ParticipantAction.HIT) {
            if (player.isActive()) {
                builder.append("Their current hand value is %d, and it's still their turn."
                        .formatted(player.getHandValue()));
            } else {
                builder.append("They have busted at a hand value of %d, and their turn is over (they lose).\n"
                                .formatted(player.getHandValue()))
                        .append(getTurnOverMessage());
            }
        } else if (participantAction == ParticipantAction.STAND) {
            builder.append("They stood with a hand value of %d. Their turn is now over.\n"
                            .formatted(player.getHandValue()))
                    .append(getTurnOverMessage());
        }
        return builder.toString();
    }

    private String getTurnOverMessage() {
        return isLastPlayerTurnOver() ? "The game is over." : "The next player is %s."
                .formatted(players.get(playerTurn).getName());
    }
}
