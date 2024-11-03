package cat.itacademy.s05.t01.service;

import cat.itacademy.s05.t01.enums.ParticipantAction;
import cat.itacademy.s05.t01.exception.custom.GameNotFoundException;
import cat.itacademy.s05.t01.model.Game;
import cat.itacademy.s05.t01.model.User;
import cat.itacademy.s05.t01.model.card.AceCard;
import cat.itacademy.s05.t01.model.card.NonAceCard;
import cat.itacademy.s05.t01.model.dto.SimplePlayer;
import cat.itacademy.s05.t01.model.participant.Player;
import cat.itacademy.s05.t01.repository.GameRepository;
import cat.itacademy.s05.t01.repository.UserRepository;
import cat.itacademy.s05.t01.service.impl.GameServiceImpl;
import cat.itacademy.s05.t01.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class GameServiceTest {
    @InjectMocks
    private GameServiceImpl gameService;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private Game mockGame;
    private List<SimplePlayer> mockList;

    private void setupMockSimplePlayersList() {
        mockList = List.of(
                new SimplePlayer("Maria", 100),
                new SimplePlayer("Lucia", 100)
        );
    }

    private void setupMockGame() {
        mockGame = new Game();
        mockGame.setPlayers(Arrays.asList(
                Player.builder().name("Maria").build(),
                Player.builder().name("Lucia").build()
        ));
    }

    @Test
    void testCreateGame() {
        setupMockSimplePlayersList();
        setupMockGame();
        when(gameRepository.save(any(Game.class))).thenReturn(Mono.just(mockGame));

        StepVerifier.create(gameService.createGame(mockList))
                .assertNext(createdGame -> assertNotNull(createdGame, "The created game should not be null"))
                .verifyComplete();
    }

    @Test
    void testGetGameDetails_whenGameExists() {
        setupMockGame();
        String gameId = "1";
        when(gameRepository.findById(gameId)).thenReturn(Mono.just(mockGame));

        StepVerifier.create(gameService.getGameDetails(gameId))
                .expectNext(mockGame)
                .verifyComplete();
    }

    @Test
    void testGetGameDetails_whenGameDoesNotExist() {
        String gameId = "nonExistentId";
        when(gameRepository.findById(gameId)).thenReturn(Mono.empty());

        StepVerifier.create(gameService.getGameDetails(gameId))
                .expectError(GameNotFoundException.class)
                .verify();
    }

    @Test
    void testMakeMove_whenGameExists_andNotLastPlayerBusts() {
        mockGame = new Game();
        Player player1 = Player.builder().name("Maria").build();
        Player player2 = Player.builder().name("Lucia").build();
        player1.setCardsInHand(new ArrayList<>
                (Arrays.asList(new NonAceCard(10), new NonAceCard(10), new AceCard())));
        mockGame.setPlayers(new ArrayList<>(Arrays.asList(player1, player2)));

        String gameId = "1";
        when(gameRepository.findById(gameId)).thenReturn(Mono.just(mockGame));
        when(gameRepository.save(any(Game.class))).thenReturn(Mono.just(mockGame));

        StepVerifier.create(gameService.makeMove(gameId, ParticipantAction.HIT))
                .assertNext(moveResponse -> {
                    assertEquals("Maria", moveResponse.getPlayerName());
                    assertEquals("HIT", moveResponse.getMove());
                    assertTrue(moveResponse.getHandValue() > 21,
                            "The hand value should be greater than 21");
                    assertFalse(moveResponse.isTurnContinues());
                    assertFalse(moveResponse.isGameOver());
                })
                .verifyComplete();

        verify(gameRepository).save(mockGame);
    }

    @Test
    void testMakeMove_whenGameExists_andLastPlayerStands() {
        setupMockGame();
        mockGame.setPlayerTurn(1);

        List<User> mockUsers = Arrays.asList(User.builder().name("Maria").build(),
                User.builder().name("Lucia").build());

        String gameId = "1";
        when(gameRepository.findById(gameId)).thenReturn(Mono.just(mockGame));
        when(gameRepository.save(any(Game.class))).thenReturn(Mono.just(mockGame));
        when(userService.insertScores(any(Game.class))).thenReturn(Flux.fromIterable(mockUsers));

        StepVerifier.create(gameService.makeMove(gameId, ParticipantAction.STAND))
                .assertNext(moveResponse -> {
                    assertEquals("Lucia", moveResponse.getPlayerName());
                    assertEquals("STAND", moveResponse.getMove());
                    assertFalse(moveResponse.isTurnContinues());
                    assertTrue(moveResponse.isGameOver());
                })
                .verifyComplete();

        verify(userService).insertScores(mockGame);
    }

    @ParameterizedTest
    @EnumSource(ParticipantAction.class)
    void testMakeMove_whenGameDoesNotExist(ParticipantAction action) {
        String gameId = "nonExistentId";
        when(gameRepository.findById(gameId)).thenReturn(Mono.empty());

        StepVerifier.create(gameService.makeMove(gameId, action))
                .expectError(GameNotFoundException.class)
                .verify();
    }

    @Test
    void testDeleteGame_whenGameExists() {
        setupMockGame();
        String gameId = "1";
        when(gameRepository.findById(gameId)).thenReturn(Mono.just(mockGame));
        when(gameRepository.deleteById(gameId)).thenReturn(Mono.empty());

        StepVerifier.create(gameService.deleteGame(gameId))
                .expectComplete()
                .verify();

        verify(gameRepository).deleteById(gameId);
    }

    @Test
    void testDeleteGame_whenGameDoesNotExist() {
        String gameId = "nonexistentId";
        when(gameRepository.findById(gameId)).thenReturn(Mono.empty());

        StepVerifier.create(gameService.deleteGame(gameId))
                .expectError(GameNotFoundException.class)
                .verify();

        verify(gameRepository, never()).deleteById(anyString());
    }
}
