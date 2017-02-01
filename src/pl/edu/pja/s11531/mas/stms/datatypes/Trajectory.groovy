package pl.edu.pja.s11531.mas.stms.datatypes

import javax.validation.constraints.NotNull
import java.time.LocalDateTime

/**
 * Trajectory of a celestial body. Can be relative (for example moon orbit to a planet, while planet has orbit relative to star).
 */
class Trajectory implements Serializable {
    @NotNull
    final TimedPath path
    final Trajectory parent

    Trajectory(@NotNull TimedPath path, Trajectory parent) {
        this.path = path
        this.parent = parent
    }

    Position calculatePosition(@NotNull LocalDateTime time) {
        return new Position(calculateOffset(time), time)
    }

    private Offset calculateOffset(@NotNull LocalDateTime time) {
        Offset offset = path.getPointAt(time)
        if (parent) {
            offset += parent.calculateOffset(time)
        }
        return offset
    }
}
