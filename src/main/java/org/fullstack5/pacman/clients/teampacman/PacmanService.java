package org.fullstack5.pacman.clients.teampacman;

import org.fullstack5.pacman.api.models.Direction;
import org.fullstack5.pacman.api.models.Maze;
import org.fullstack5.pacman.api.models.Piece;
import org.fullstack5.pacman.api.models.Position;
import org.fullstack5.pacman.api.models.request.MoveRequest;
import org.fullstack5.pacman.api.models.response.GameState;
import org.fullstack5.pacman.api.models.response.MovingPiece;
import org.fullstack5.pacman.api.models.response.PlayerRegistered;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PacmanService {

    public MoveRequest determineNextMove(String gameId, PlayerRegistered playerRegistered, GameState s) {

        List<Position> allDotsPallets = new ArrayList<>();
        allDotsPallets.addAll(s.getRemainingDots());
        allDotsPallets.addAll(s.getRemainingPellets());

        Direction nextDirection = shortestDisDot(s.getPacman(), playerRegistered.getMaze(), allDotsPallets);

        return new MoveRequest(gameId, playerRegistered.getAuthId(), nextDirection, Piece.Type.PACMAN);
    }

    public Direction shortestDisDot(MovingPiece pacman, Maze maze, List<Position> dotsAndPallets) {

        Map<Direction, Position> availableDirections = getAvailableDirections(maze, pacman);

        return availableDirections.entrySet().stream()
                .filter(position -> isDotOrPallet( dotsAndPallets, position.getValue()))
                .findFirst()
                .orElse(availableDirections.entrySet().stream().findFirst().get()).getKey();
    }


    public static Map<Direction, Position> getAvailableDirections(final Maze maze, final MovingPiece piece) {
        final Map<Direction, Position> result = new HashMap<>();
        final int x = piece.getCurrentPosition().getX();
        final int y = piece.getCurrentPosition().getY();

        Position pos = new Position(x, y - 1);
        if (y == 0 || !isWall(pos, maze)) {
            result.put(Direction.NORTH, new Position(x, y - 1));
        }

        pos = new Position(x + 1, y);
        if (x == maze.getWidth() - 1 || !isWall(pos, maze)) {
            result.put(Direction.EAST, pos);
        }

        pos = new Position(x, y + 1);
        if (y == maze.getHeight() - 1 || !isWall(pos, maze)) {
            result.put(Direction.SOUTH, pos);
        }

        pos = new Position(x - 1, y);
        if (x == 0 || !maze.isWall(pos)) {
            result.put(Direction.WEST, pos);
        }
        return result;
    }

    public static boolean isWall(Position position, Maze maze) {
        return maze.isWall(position.getX(), position.getY());
    }


    public static boolean isDotOrPallet(List<Position> dotsPallets, Position position) {
        System.out.println("remaining dots:  "+ dotsPallets.size());
        return dotsPallets.contains(position);
    }
}
