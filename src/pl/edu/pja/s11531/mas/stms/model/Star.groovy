package pl.edu.pja.s11531.mas.stms.model

import pl.edu.pja.s11531.mas.stms.datatypes.Radiation
import pl.edu.pja.s11531.mas.stms.datatypes.RadiationSource

/**
 * Large ball of fire. Usually quite bright and hot. It is advised not to fly nearby.
 */
class Star extends StarSystemObject {
    double mass
    final Map<Radiation.Type, RadiationSource> radiation = new HashMap<>()
}
