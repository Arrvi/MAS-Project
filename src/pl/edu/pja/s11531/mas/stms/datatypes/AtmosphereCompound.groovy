package pl.edu.pja.s11531.mas.stms.datatypes

/**
 * Atmosphere compound. Misc type. Pressure is measured in hPa
 * Usually (oxygen, 210), (nitrogen, 800), etc.
 */
class AtmosphereCompound implements Serializable {
    final String name
    final BigDecimal pressure

    AtmosphereCompound(String name, BigDecimal pressure) {
        this.name = name
        this.pressure = pressure
    }
}
