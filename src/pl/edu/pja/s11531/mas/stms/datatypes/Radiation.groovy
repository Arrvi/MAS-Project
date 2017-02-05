package pl.edu.pja.s11531.mas.stms.datatypes

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

import javax.validation.constraints.NotNull

/**
 * Radiation intensity with type. Misc type.
 */
class Radiation implements DataType {
    /**
     * Approximate intensity of radiation resulting in death of adult human.
     */
    static final BigDecimal FATAL_INTENSITY = 100.0

    /**
     * Intensity of this radiation in some arbitrary unit
     */
    @NotNull
    final BigDecimal intensity
    /**
     * Type of this radiation. Does not really matter.
     */
    @NotNull
    final Type type

    @JsonCreator
    Radiation(
            @JsonProperty('intensity') @NotNull BigDecimal intensity,
            @JsonProperty('type') @NotNull Type type) {
        this.intensity = intensity
        this.type = type
    }

    Radiation(@NotNull Radiation radiation) {
        this.intensity = radiation.intensity
        this.type = radiation.type
    }

    static enum Type {
        ALPHA, BETA, GAMMA
    }
}
