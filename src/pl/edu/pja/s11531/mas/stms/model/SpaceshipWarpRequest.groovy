package pl.edu.pja.s11531.mas.stms.model

import pl.edu.pja.s11531.mas.stms.constraints.CompositionCheck
import pl.edu.pja.s11531.mas.stms.persistence.DatabaseObject
import pl.edu.pja.s11531.mas.stms.persistence.LinkedObject

import javax.validation.constraints.NotNull

/**
 * Information about a spaceship during jump sequence.
 */
class SpaceshipWarpRequest extends LinkedObject implements DatabaseObject, CompositionCheck {
    @NotNull
    Spaceship spaceship
    @NotNull
    WarpRequest request
    int crewCount
    final List<String> cargo = new LinkedList<>()
    @NotNull
    String owner
    @NotNull
    String captain
    double additionalMass

    void setSpaceship(Spaceship ship) {
        checkComposition(spaceship, ship)
        spaceship = ship
        ship?.link(this, false)
    }

    void setRequest(WarpRequest request) {
        checkComposition(this.request, request)
        this.request = request
        request?.link(this, false)
    }

    @Override
    protected Map<Class, String> getLinkProperties() {
        return super.getLinkProperties() + [
                (Spaceship.class)  : 'spaceship',
                (WarpRequest.class): 'request'
        ]
    }
}
