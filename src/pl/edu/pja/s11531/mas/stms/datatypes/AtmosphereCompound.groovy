package pl.edu.pja.s11531.mas.stms.datatypes

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

import javax.validation.constraints.NotNull

/**
 * Atmosphere compound. Misc type. Pressure is measured in hPa
 * Usually (oxygen, 210), (nitrogen, 800), etc.
 */
class AtmosphereCompound implements DataType {
    @NotNull
    final String name
    final double pressure

    @JsonCreator
    AtmosphereCompound(
            @JsonProperty('name') @NotNull String name,
            @JsonProperty('pressure') double pressure) {
        this.name = name
        this.pressure = pressure
    }
}
