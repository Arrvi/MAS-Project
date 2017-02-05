package pl.edu.pja.s11531.mas.stms.datatypes

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

import javax.validation.constraints.NotNull
import java.time.LocalDateTime

/**
 * Trajectory of a celestial body. Can be relative (for example moon orbit to a planet, while planet has orbit relative to star).
 */
class Trajectory implements Serializable {
    /**
     * Path of this trajectory.
     */
    @NotNull
    final TimedPath path
    /**
     * Parent of this trajectory. Its position is used as reference point for calculation. Null means [0,0,0] reference point.
     */
    final Trajectory parent

    @JsonCreator
    Trajectory(
            @JsonProperty('path') @NotNull TimedPath path,
            @JsonProperty('parent') Trajectory parent) {
        this.path = path
        this.parent = parent
    }

    /**
     * Calculates position of this trajectory at given time. Positions of parent trajectories are taken into account.
     * @param time
     * @return
     */
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
