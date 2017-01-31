package pl.edu.pja.s11531.mas.stms.model

import pl.edu.pja.s11531.mas.stms.persistence.LinkedObject

import javax.validation.constraints.NotNull

/**
 * A registered spaceship.
 */
class Spaceship implements LinkedObject {
    final static BigDecimal SPEED = 1.0

    SpaceshipType type
    @NotNull
    String name
    double mass
    @NotNull
    String currentOwner
    @NotNull
    String currentCaptain
}
