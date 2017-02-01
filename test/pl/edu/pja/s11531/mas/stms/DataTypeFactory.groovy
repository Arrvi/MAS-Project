package pl.edu.pja.s11531.mas.stms

import pl.edu.pja.s11531.mas.stms.datatypes.*

import java.time.LocalDateTime
import java.time.Period

/**
 * Creates dummy objects
 */
class DataTypeFactory {
    static LocalDateTime past() {
        now().minusMonths(1)
    }

    static LocalDateTime now() {
        LocalDateTime.now()
    }

    static LocalDateTime future() {
        now().plusMonths(1)
    }

    static Period year() {
        Period.ofYears(1)
    }
    /**
     * @return 8-point clockwise 'circle' with radius of 1 au on xy plane
     */
    static Path path() {
        def au = Distance.au(1.0)
        def au07 = Distance.au(0.707107)
        def z = BigDecimal.ZERO

        Path.of(
                [z, au, z],
                [au07, au07, z],
                [au, z, z],
                [au07, -au07, z],
                [z, -au, z],
                [-au07, -au07, z],
                [-au, z, z],
                [-au07, au07, z],
                [z, au, z]
        )
    }

    static Path pointPath() {
        Path.of([0.0, 0.0, 0.0], [0.0, 0.0, 0.0])
    }

    static TimedPath timedPath() {
        new TimedPath(path(), past(), year())
    }

    static TimedPath pointTimedPath() {
        new TimedPath(pointPath(), now(), year())
    }

    static Trajectory trajectory(Trajectory parent = null) {
        new Trajectory(timedPath(), parent)
    }

    static Trajectory staticTrajectory(Trajectory parent = null) {
        new Trajectory(pointTimedPath(), parent)
    }

    static AtmosphereCompound oxygen() {
        new AtmosphereCompound("oxygen", 300)
    }

    static AtmosphereCompound nitrogen() {
        new AtmosphereCompound("nitrogen", 700)
    }
}
