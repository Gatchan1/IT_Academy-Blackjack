package cat.itacademy.s05.t01.model;

import cat.itacademy.s05.t01.model.card.AceCard;
import cat.itacademy.s05.t01.model.card.Card;
import cat.itacademy.s05.t01.model.card.NonAceCard;
import cat.itacademy.s05.t01.model.enums.ParticipantAction;
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
    @Setter private List<? extends Card> undealtCards = generateInitialCardList();;
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

    public String makeMove(ParticipantAction participantAction) {
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
                }
                else croupier.stand();
            }
            isActive = false;
        }
        return getMoveInfo(playerThatMakesMove, participantAction);
    }

    private String getMoveInfo(Player player, ParticipantAction participantAction) {
        StringBuilder builder = new StringBuilder("Move Result: \n");
        if (participantAction == ParticipantAction.HIT) {
            builder.append("""
            %s has made a "hit" move.
            """.formatted(player.getName()));
            if (player.isActive()) {
                builder.append("""
                Their current hand value is %d,
                and it's still their turn.
                """.formatted(player.getHandValue()));
            } else {
                builder.append("""
                They have busted at a hand value of %d,
                and their turn is over (they lose).
                """.formatted(player.getHandValue()));
                if(isLastPlayerTurnOver()) {
                    builder.append("The game is now over.");
                } else {
                    builder.append("""
                    The next player is %s.""".formatted(players.get(playerTurn).getName()));
                }
            }
        } else if (participantAction == ParticipantAction.STAND) {
            builder.append("""
            %s has made a "stand" move, with a hand value of %d.
            Their turn is now over.
            """.formatted(player.getName(), player.getHandValue()));
            if(isLastPlayerTurnOver()) {
                builder.append("The game is also over.");
            } else {
                builder.append("""
                    The next player is %s.""".formatted(players.get(playerTurn).getName()));
            }
        }
        return builder.toString();
    }
}
