package pl.edu.pja.s11531.mas.stms.datatypes

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

import javax.validation.constraints.NotNull

/**
 * Radiation source. Misc type.
 */
class RadiationSource extends Radiation {
    @JsonCreator
    RadiationSource(
            @JsonProperty('intensity') @NotNull BigDecimal intensity,
            @JsonProperty('type') @NotNull Type type) {
        super(intensity, type)
    }

    RadiationSource(@NotNull Radiation radiation) {
        super(radiation)
    }

    BigDecimal calculateRadiationAt(@NotNull BigDecimal distance) {
        intensity / distance**2
    }
}
