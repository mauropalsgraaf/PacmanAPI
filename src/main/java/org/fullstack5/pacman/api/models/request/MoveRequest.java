package org.fullstack5.pacman.api.models.request;

import lombok.*;
import org.fullstack5.pacman.api.models.Piece;
import org.fullstack5.pacman.api.models.Direction;

/**
 * Class for the request data for moving a piece
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public final class MoveRequest {
    private String gameId;
    private String authId;
    private Direction direction;
    private Piece.Type type;
}
