package pl.edu.pja.s11531.mas.stms.datatypes

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

import javax.validation.constraints.NotNull
import java.time.LocalDateTime
import java.time.Period

/**
 * Path on which object travels with constant speed
 */
class TimedPath implements Serializable {
    /**
     * Path on which this object travels
     */
    @NotNull
    final Path path
    /**
     * Time of phase zero
     */
    @NotNull
    final LocalDateTime timeZero
    /**
     * Time needed to complete this path
     */
    @NotNull
    final Period cycleDuration

    @JsonCreator
    TimedPath(
            @JsonProperty('path') @NotNull Path path,
            @JsonProperty('timeZero') @NotNull LocalDateTime timeZero,
            @JsonProperty('cycleDuration') @NotNull Period cycleDuration) {
        this.path = path
        this.timeZero = timeZero
        this.cycleDuration = cycleDuration
    }

    /**
     * Returns stationary path. Useful for star systems.
     * @param position point at which object stays
     * @return stationary path with period of 1 year (arbitrary value)
     */
    static TimedPath stationary(Position position) {
        return new TimedPath(new Path([position, position]), position.time, Period.of(1, 0, 0))
    }

    /**
     * Calculates position on this path at given time
     * @param time
     * @return position on this path at given time
     */
    Offset getPointAt(@NotNull LocalDateTime time) {
        def currCycleTime = Period.between(timeZero.toLocalDate(), time.toLocalDate())
        if (currCycleTime.isNegative()) {
            while (currCycleTime.isNegative()) {
                currCycleTime += cycleDuration
            }
        } else {
            while (!(currCycleTime - cycleDuration).negative) {
                currCycleTime -= cycleDuration
            }
        }
        return path.getPointAt(getDurationRatio(currCycleTime, cycleDuration))
    }

    private static double getDurationRatio(@NotNull Period numerator, @NotNull Period denominator) {
        if (denominator.getDays() == 0) return 0;
        return numerator.getDays() / denominator.getDays()
    }
}
