package pl.edu.pja.s11531.mas.stms.model

import pl.edu.pja.s11531.mas.stms.persistence.ConstantsProvider
import pl.edu.pja.s11531.mas.stms.persistence.DatabaseObject
import pl.edu.pja.s11531.mas.stms.persistence.LinkedObject

import javax.validation.constraints.NotNull

/**
 * A registered spaceship.
 */
class Spaceship extends LinkedObject implements DatabaseObject {
    final static BigDecimal SPEED = new BigDecimal(ConstantsProvider.SPACESHIP_SPEED)

    SpaceshipType type
    @NotNull
    String name
    double mass
    @NotNull
    String currentOwner
    @NotNull
    String currentCaptain

    @Override
    protected Map<Class, String> getLinkProperties() {
        return super.getLinkProperties() + [(SpaceshipType.class): 'type']
    }
}
