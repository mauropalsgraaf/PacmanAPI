package org.fullstack5.pacman.clients.teampacman;

import org.fullstack5.pacman.api.models.PlayerType;
import org.fullstack5.pacman.api.models.request.MoveRequest;
import org.fullstack5.pacman.api.models.request.RegisterGameRequest;
import org.fullstack5.pacman.api.models.request.RegisterPlayerRequest;
import org.fullstack5.pacman.api.models.request.StateRequest;
import org.fullstack5.pacman.api.models.response.GameRegistered;
import org.fullstack5.pacman.api.models.response.GameState;
import org.fullstack5.pacman.api.models.response.PlayerRegistered;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 *  class taking care of communications from client to server.
 */
public final class ServerComm {

    private final String url;

    public ServerComm(final String url) {
        this.url = url;
    }

    public PlayerRegistered registerPlayer(final String gameId, final PlayerType playerType) {
        final RegisterPlayerRequest request = new RegisterPlayerRequest(gameId, playerType);
        System.out.println("url = " + url);
        return WebClient.create(this.url).post()
                .uri("/register-player")
                .body(BodyInserters.fromObject(request))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(PlayerRegistered.class)
                .block();
    }

    public void performMove(final MoveRequest request) {
        WebClient.create(this.url).post()
                .uri("/perform-move")
                .body(BodyInserters.fromObject(request))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve();
    }

    /**
     * @return the game id.
     */
     public String startGame() {
        final RegisterGameRequest request = new RegisterGameRequest(Duration.ofMillis(100L));
        return WebClient.create(this.url).post()
                .uri("/register-game")
                .body(BodyInserters.fromObject(request))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(GameRegistered.class)
                .block()
                .getGameId();
    }

    public Flux<GameState> establishGameStateFlux(final String gameId) {
         System.out.println("being called");
        final StateRequest request = new StateRequest(gameId);
        final Flux<GameState> f = WebClient.create(this.url).post()
                .uri("/current-state")
                .body(BodyInserters.fromObject(request))
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .retrieve()
                .bodyToFlux(GameState.class)
                .doOnError(throwable -> {
                    System.out.println("Stream terminated remotely. Exiting");
                    System.exit(1);
                });

        System.out.println(f);
        return f;
    }

}
