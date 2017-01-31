package pl.edu.pja.s11531.mas.stms.datatypes

import javax.validation.constraints.NotNull
import java.time.Duration
import java.time.LocalDateTime

/**
 * Created by kris on 1/30/17.
 */
class TimedPath implements Serializable {
    @NotNull
    final Path path
    @NotNull
    final LocalDateTime timeZero
    @NotNull
    final Duration cycleDuration

    TimedPath(@NotNull Path path, @NotNull LocalDateTime timeZero, @NotNull Duration cycleDuration) {
        this.path = path
        this.timeZero = timeZero
        this.cycleDuration = cycleDuration
    }

    Offset getPointAt(@NotNull LocalDateTime time) {
        def currCycleTime = Duration.between(timeZero, time)
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

    private static double getDurationRatio(@NotNull Duration numerator, @NotNull Duration denominator) {
        try {
            return numerator.toNanos() / denominator.toNanos()
        } catch (ArithmeticException ignored) {

        }
        try {
            return numerator.toMillis() / denominator.toMillis()
        } catch (ArithmeticException ignored) {

        }
        return numerator.getSeconds() / denominator.getSeconds()
    }
}
