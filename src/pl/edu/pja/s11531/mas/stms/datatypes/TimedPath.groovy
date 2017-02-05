package pl.edu.pja.s11531.mas.stms.datatypes

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

import javax.validation.constraints.NotNull
import java.time.LocalDateTime
import java.time.Period

/**
 * Created by kris on 1/30/17.
 */
class TimedPath implements Serializable {
    @NotNull
    final Path path
    @NotNull
    final LocalDateTime timeZero
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

    static TimedPath stationary(Position position) {
        return new TimedPath(new Path([position, position]), position.time, Period.of(1, 0, 0))
    }

    Offset getPointAt(@NotNull LocalDateTime time) {
        def currCycleTime = Period.between(timeZero.toLocalDate(), time.toLocalDate())
        if (currCycleTime.isNegative()) {
            while (currCycleTime.isNegative()) {
                currCycleTime += cycleDuration
            }
        } else {
            while (currCycleTime > cycleDuration) {
                currCycleTime -= cycleDuration
            }
        }
        return path.getPointAt(getDurationRatio(currCycleTime, cycleDuration))
    }

    private static double getDurationRatio(@NotNull Period numerator, @NotNull Period denominator) {
        return numerator.getDays() / denominator.getDays()
    }
}
