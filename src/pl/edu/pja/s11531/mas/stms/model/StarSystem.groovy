package pl.edu.pja.s11531.mas.stms.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Group of objects orbiting around some star.
 */
@JsonIgnoreProperties('gates')
class StarSystem extends SpaceObject {
    final Set<WarpGate> gates = []

    @Override
    protected Map<Class, String> getLinkProperties() {
        return super.getLinkProperties() + [
                (WarpGate.class): 'gates'
        ]
    }
}
