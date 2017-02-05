package pl.edu.pja.s11531.mas.stms.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import pl.edu.pja.s11531.mas.stms.persistence.ConfigObject
import pl.edu.pja.s11531.mas.stms.persistence.LinkedObject

import javax.validation.constraints.NotNull

/**
 * Spaceship type or model.
 */
@JsonIgnoreProperties(['ships'])
class SpaceshipType extends LinkedObject implements ConfigObject {
    @NotNull
    String name

    List<Spaceship> getShips() {
        getExtent(Spaceship.class).findAll { it.type == this }
    }

    @Override
    protected Map<Class, String> getLinkProperties() {
        return super.getLinkProperties() + [(Spaceship.class): 'ships']
    }
}
