package pl.edu.pja.s11531.mas.stms.model

import pl.edu.pja.s11531.mas.stms.datatypes.Trajectory
import pl.edu.pja.s11531.mas.stms.persistence.ConfigObject
import pl.edu.pja.s11531.mas.stms.persistence.LinkedObject

import javax.validation.constraints.NotNull

/**
 * Any object in space with trackable position. Stationary or on known trajectory
 */
abstract class SpaceObject implements LinkedObject, ConfigObject {
    @NotNull
    Trajectory trajectory
    @NotNull
    String name
}
