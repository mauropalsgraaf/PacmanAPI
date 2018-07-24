package org.fullstack5.pacman.clients.teampacman.pacman;

import lombok.AllArgsConstructor;
import org.fullstack5.pacman.api.models.Direction;
import org.fullstack5.pacman.api.models.Maze;
import org.fullstack5.pacman.api.models.Piece;
import org.fullstack5.pacman.api.models.request.MoveRequest;
import org.fullstack5.pacman.api.models.response.GameState;
import org.fullstack5.pacman.clients.teampacman.AI;
import org.fullstack5.pacman.clients.teampacman.ServerComm;

@AllArgsConstructor
public class ReinforcementPacmanAI implements AI {

    private final String gameId;
    private final String authId;
    private final Maze maze;
    private final ServerComm comm;

    @Override
    public void runAI(GameState gameState) {
//        Direction direction = null;
//
//        double x = Math.random() % 2;
//        if (x == 0) {
//            direction = Direction.WEST;
//        } else {
//            direction = Direction.EAST;
//        }
//
        System.out.println("Direction: " + Direction.EAST);

        final MoveRequest request = new MoveRequest(gameId, authId, Direction.EAST, Piece.Type.PACMAN);

        comm.performMove(request);
    }
}
