package pl.edu.pja.s11531.mas.stms.datatypes

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

    TimedPath(@NotNull Path path, @NotNull LocalDateTime timeZero, @NotNull Period cycleDuration) {
        this.path = path
        this.timeZero = timeZero
        this.cycleDuration = cycleDuration
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
