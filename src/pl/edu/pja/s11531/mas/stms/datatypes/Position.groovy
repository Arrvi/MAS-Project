package pl.edu.pja.s11531.mas.stms.datatypes

import java.time.LocalDateTime

/**
 * Absolute position in Universe at given time. From some commonly accepted point.
 *
 * Let's pretend that's possible to measure.
 */
class Position extends Offset {
    final LocalDateTime time

    Position(double x, double y, double z, LocalDateTime time) {
        super(x, y, z)
        this.time = time
    }

    Position(Offset offset, LocalDateTime time) {
        super(offset)
        this.time = time
    }
}
