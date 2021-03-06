package org.fullstack5.pacman.clients.teampacman;

import org.fullstack5.pacman.api.models.PlayerType;
import org.fullstack5.pacman.api.models.response.GameState;
import org.fullstack5.pacman.api.models.response.PlayerRegistered;
import org.fullstack5.pacman.clients.teampacman.ghosts.AStarGhostAI;
import org.fullstack5.pacman.clients.teampacman.pacman.RandomPacmanAI;
import org.fullstack5.pacman.clients.teampacman.pacman.ReinforcementPacmanAI;
import reactor.core.publisher.Flux;

public final class TeamPacmanClient implements Runnable {

    private final String hostAndPort;
    private final String gameId;

    public TeamPacmanClient(final String hostAndPort, final String gameId) {
        this.hostAndPort = hostAndPort;
        this.gameId = gameId;
    }

    @Override
    public final void run() {
        startGameAsGhosts();
    }

    private void startGameAsGhosts() {
        final ServerComm comm = new ServerComm(hostAndPort);
        final String gameId = comm.startGame();
        final Flux<GameState> flux = comm.establishGameStateFlux(gameId);

        // System.out.println("registerPlayer");
        final PlayerRegistered pacmanPlayer = comm.registerPlayer(gameId, PlayerType.PACMAN);
        // System.out.println("beforeRunningthead");
        final RunnerThread pacmanThread = new RunnerThread(new ReinforcementPacmanAI(gameId, pacmanPlayer.getAuthId(), pacmanPlayer.getMaze(), comm));
        // System.out.println("after");
        flux.subscribe(pacmanThread::updateState);

        final PlayerRegistered ghostPlayer = comm.registerPlayer(gameId, PlayerType.GHOSTS);
        final RunnerThread ghostThread = new RunnerThread(new AStarGhostAI(gameId, ghostPlayer.getAuthId(), ghostPlayer.getMaze()));
        flux.subscribe(ghostThread::updateState);

    }

    public static void main(final String...args) {
        String hostAndPort = "http://localhost:8080";
        String gameId = "nckw";
        new TeamPacmanClient(hostAndPort, gameId).run();
    }

}
