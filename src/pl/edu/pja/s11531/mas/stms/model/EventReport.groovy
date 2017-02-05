package pl.edu.pja.s11531.mas.stms.model

import pl.edu.pja.s11531.mas.stms.constraints.CompositionCheck
import pl.edu.pja.s11531.mas.stms.persistence.DatabaseObject
import pl.edu.pja.s11531.mas.stms.persistence.LinkedObject

import javax.validation.constraints.NotNull
import java.time.LocalDateTime

/**
 * Report of an event that occurred during jump sequence.
 */
class EventReport extends LinkedObject implements DatabaseObject, CompositionCheck {
    @NotNull
    WarpRequest request
    @NotNull
    LocalDateTime time
    @NotNull
    Type type
    @NotNull
    String description

    void setRequest(WarpRequest request) {
        checkComposition(this.request, request)
        this.request = request
        request?.link(this, false)
    }

    @Override
    protected Map<Class, String> getLinkProperties() {
        return super.getLinkProperties() + [(WarpRequest.class): 'request']
    }

    static enum Type {
        SPACESHIP_MALFUNCTION,
        GATE_MALFUNCTION,
        COLLISION,
        UNAUTHORIZED_WARP,
        PIRATE_ATTACK,
        CREW_HEALTH_PROBLEM,
        OTHER
    }
}
