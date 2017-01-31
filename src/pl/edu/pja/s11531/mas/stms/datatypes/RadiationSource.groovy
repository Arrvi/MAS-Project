package pl.edu.pja.s11531.mas.stms.datatypes

import javax.validation.constraints.NotNull

/**
 * Radiation source. Misc type.
 */
class RadiationSource extends Radiation {
    RadiationSource(@NotNull BigDecimal intensity, @NotNull Type type) {
        super(intensity, type)
    }

    RadiationSource(@NotNull Radiation radiation) {
        super(radiation)
    }

    BigDecimal calculateRadiationAt(@NotNull BigDecimal distance) {
        intensity / distance**2
    }
}
