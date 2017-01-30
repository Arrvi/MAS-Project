package pl.edu.pja.s11531.mas.stms.datatypes

import java.time.LocalDateTime

/**
 * Trajectory of a celestial body. Can be relative (for example moon orbit to a planet, while planet has orbit relative to star).
 */
class Trajectory implements Serializable {
    final TimedPath path
    final Trajectory parent

    Trajectory(TimedPath path, Trajectory parent) {
        this.path = path
        this.parent = parent
    }

    Position calculatePosition(LocalDateTime time) {
        return new Position(calculateOffset(time), time)
    }

    private Offset calculateOffset(LocalDateTime time) {
        Offset offset = path.getPointAt(time)
        if (parent) {
            offset += parent.calculateOffset(time)
        }
        return offset
    }
}
