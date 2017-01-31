package pl.edu.pja.s11531.mas.stms.datatypes

import javax.validation.constraints.NotNull
import java.time.LocalDateTime

/**
 * Absolute position in Universe at given time. From some commonly accepted point.
 *
 * Let's pretend that's possible to measure.
 */
class Position extends Offset {
    final LocalDateTime time

    Position(@NotNull BigDecimal x, @NotNull BigDecimal y, @NotNull BigDecimal z, @NotNull LocalDateTime time) {
        super(x, y, z)
        this.time = time
    }

    Position(@NotNull Offset offset, @NotNull LocalDateTime time) {
        super(offset)
        this.time = time
    }
}
