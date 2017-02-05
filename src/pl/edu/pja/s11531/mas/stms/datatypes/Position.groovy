package pl.edu.pja.s11531.mas.stms.datatypes

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

import javax.validation.constraints.NotNull
import java.time.LocalDateTime

/**
 * Absolute position in Universe at given time. From some commonly accepted point.
 *
 * Let's pretend that's possible to measure.
 */
class Position extends Offset {
    /**
     * Time at which this position was defined
     */
    final LocalDateTime time

    @JsonCreator
    Position(
            @JsonProperty('x') @NotNull BigDecimal x,
            @JsonProperty('y') @NotNull BigDecimal y,
            @JsonProperty('z') @NotNull BigDecimal z,
            @JsonProperty('time') @NotNull LocalDateTime time) {
        super(x, y, z)
        this.time = time
    }

    Position(@NotNull Offset offset, @NotNull LocalDateTime time) {
        super(offset)
        this.time = time
    }
}
