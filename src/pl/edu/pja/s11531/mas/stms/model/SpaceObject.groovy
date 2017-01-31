package pl.edu.pja.s11531.mas.stms.model

import pl.edu.pja.s11531.mas.stms.persistence.ConfigObject
import pl.edu.pja.s11531.mas.stms.persistence.LinkedObject

/**
 * Any object in space with trackable position. Stationary or on known trajectory
 */
abstract class SpaceObject implements LinkedObject, ConfigObject {
}
