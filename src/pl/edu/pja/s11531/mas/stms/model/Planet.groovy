package pl.edu.pja.s11531.mas.stms.model

import pl.edu.pja.s11531.mas.stms.datatypes.AtmosphereCompound

/**
 * A planet. Might not be habitable
 */
class Planet extends StarSystemObject {
    final Set<AtmosphereCompound> atmosphereCompounds = new HashSet<>()
    double radius
    double mass

    void setAtmosphereCompounds(Collection<AtmosphereCompound> compounds) {
        atmosphereCompounds.clear()
        atmosphereCompounds.addAll(compounds)
    }
}
