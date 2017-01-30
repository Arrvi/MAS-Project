package pl.edu.pja.s11531.mas.stms.datatypes

/**
 * Radiation intensity with type. Misc type.
 */
class Radiation implements Serializable {
    final BigDecimal intensity
    final Type type

    Radiation(BigDecimal intensity, Type type) {
        this.intensity = intensity
        this.type = type
    }

    static enum Type {
        ALPHA, BETA, GAMMA
    }
}
