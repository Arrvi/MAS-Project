package pl.edu.pja.s11531.mas.stms.model

import pl.edu.pja.s11531.mas.stms.persistence.ConfigObject
import pl.edu.pja.s11531.mas.stms.persistence.LinkedObject

import javax.validation.constraints.NotNull

/**
 * Spaceship type or model.
 */
class SpaceshipType implements LinkedObject, ConfigObject {
    @NotNull
    String name
}
