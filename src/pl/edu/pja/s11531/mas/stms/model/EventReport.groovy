package pl.edu.pja.s11531.mas.stms.model

import pl.edu.pja.s11531.mas.stms.persistence.LinkedObject

import javax.validation.constraints.NotNull
import java.time.LocalDateTime

/**
 * Report of an event that occurred during jump sequence.
 */
class EventReport implements LinkedObject {
    @NotNull
    LocalDateTime time
    @NotNull
    Type type
    @NotNull
    String description

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
