package pl.edu.pja.s11531.mas.stms.datatypes

import java.time.Duration
import java.time.LocalDateTime

/**
 * Created by kris on 1/30/17.
 */
class TimedPath implements Serializable {
    final Path path
    final LocalDateTime timeZero
    final Duration cycleDuration

    TimedPath(Path path, LocalDateTime timeZero, Duration cycleDuration) {
        this.path = path
        this.timeZero = timeZero
        this.cycleDuration = cycleDuration
    }

    Offset getPointAt(LocalDateTime time) {
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

    private static double getDurationRatio(Duration numerator, Duration denominator) {
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
