package org.fullstack5.pacman.clients.teampacman;

import org.fullstack5.pacman.api.models.*;
import org.fullstack5.pacman.api.models.request.MoveRequest;
import org.fullstack5.pacman.api.models.response.GameState;
import org.fullstack5.pacman.api.models.response.PlayerRegistered;
import reactor.core.publisher.Flux;

public class Main implements Runnable {

    private final String hostAndPort;
    private final String gameId;

    public Main(final String hostAndPort, final String gameId) {
        this.hostAndPort = hostAndPort;
        this.gameId = gameId;
    }

    public static void main(String[] args) {
        String hostAndPort = args[0];
        System.out.println("hostAndPort = " + hostAndPort);
        String gameId = args[1];

        new Main(hostAndPort, gameId).run();
    }

    @Override
    public void run() {
        PlayerType playerType = PlayerType.PACMAN;

        ServerComm comm = new ServerComm(hostAndPort);

        PlayerRegistered registered = comm.registerPlayer(gameId, playerType);
        PacmanService service = new PacmanService();

        Flux<GameState> state = comm.establishGameStateFlux(gameId);

        boolean exit = false;

        state.subscribe(s -> {
            MoveRequest request = service.determineNextMove(gameId, registered, s);
            comm.performMove(request);
        });

        while(true) {}
    }
}
