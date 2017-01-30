package pl.edu.pja.s11531.mas.stms.datatypes

/**
 * Radiation source. Misc type.
 */
class RadiationSource extends Radiation {
    RadiationSource(BigDecimal intensity, Type type) {
        super(intensity, type)
    }

    BigDecimal calculateRadiationAt(BigDecimal distance) {
        intensity / distance**2
    }
}
