package cat.itacademy.s05.t01.controller;

import cat.itacademy.s05.t01.enums.ParticipantAction;
import cat.itacademy.s05.t01.exception.custom.GameNotFoundException;
import cat.itacademy.s05.t01.model.Game;
import cat.itacademy.s05.t01.model.dto.MoveResponse;
import cat.itacademy.s05.t01.model.participant.Player;
import cat.itacademy.s05.t01.service.impl.GameServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(GameController.class)
public class GameControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GameServiceImpl gameService;

    private static final String BASE_URI = "/game/";
    private Game mockGame;

    private void setupMockGame() {
        mockGame = new Game();
        mockGame.setPlayers(Arrays.asList(
                Player.builder().name("Maria").build(),
                Player.builder().name("Lucia").build()
        ));
        mockGame.dealInitialCards();
    }

    @Test
    void testCreateGame_whenProperInput() {
        setupMockGame();
        when(gameService.createGame(anyList())).thenReturn(Mono.just(mockGame));

        webTestClient.post().uri(BASE_URI + "new")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("[{\"name\": \"PlayerName\"}]")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("/game/" + mockGame.getId())
                .expectBody(Game.class).isEqualTo(mockGame);
    }

    @Test
    void testCreateGame_whenIncorrectInput() {
        String requestBody = "[{\"nme\": \"PlayerName\"}]";

        webTestClient.post().uri(BASE_URI + "new")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest() // Expect a 400 Bad Request due to validation errors
                .expectBody(String.class)
                .value(response -> {
                    // Optionally, check the response body for expected validation messages
                    assertTrue(response.contains("Validation failure:"), "Should contain validation failure message");
                });
    }

    @Test
    void testGetGameDetails_whenGameExists() {
        setupMockGame();
        String gameId = "1";
        when(gameService.getGameDetails(gameId)).thenReturn(Mono.just(mockGame));

        webTestClient.get().uri(BASE_URI + gameId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Game.class).isEqualTo(mockGame);
    }

    @Test
    void testGetGameDetails_whenGameDoesNotExist() {
        String gameId = "nonExistentId";
        when(gameService.getGameDetails(gameId))
                .thenReturn(Mono.error(new GameNotFoundException("Game not found")));

        webTestClient.get().uri(BASE_URI + gameId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @ParameterizedTest
    @EnumSource(ParticipantAction.class)
    void testMakeMove_whenGameExists(ParticipantAction action) {
        String gameId = "1";
        when(gameService.makeMove(gameId, action))
                .thenReturn(Mono.just(MoveResponse.builder().build()));

        webTestClient.post().uri(BASE_URI + gameId + "/play")
                .bodyValue(action)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MoveResponse.class);
    }

    @ParameterizedTest
    @EnumSource(ParticipantAction.class)
    void testMakeMove_whenGameDoesNotExist(ParticipantAction action) {
        String gameId = "nonExistentId";
        when(gameService.makeMove(gameId, action))
                .thenReturn(Mono.error(new GameNotFoundException("Game not found")));

        webTestClient.post().uri(BASE_URI + gameId + "/play")
                .bodyValue(action)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteGame_whenGameExists() {
        String gameId = "1";
        when(gameService.deleteGame(gameId)).thenReturn(Mono.empty());

        webTestClient.delete().uri(BASE_URI + gameId + "/delete")
                .exchange()
                .expectStatus().isNoContent();

        verify(gameService).deleteGame(gameId);
    }

    @Test
    void testDeleteGame_whenGameDoesNotExist() {
        String gameId = "nonExistentId";
        when(gameService.deleteGame(gameId))
                .thenReturn(Mono.error(new GameNotFoundException("Game not found")));

        webTestClient.delete().uri(BASE_URI + gameId + "/delete")
                .exchange()
                .expectStatus().isNotFound();
    }

}
