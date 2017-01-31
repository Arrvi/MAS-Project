package pl.edu.pja.s11531.mas.stms.model

import pl.edu.pja.s11531.mas.stms.persistence.LinkedObject

import javax.validation.constraints.NotNull

/**
 * Information about a spaceship during jump sequence.
 */
class SpaceshipWarpRequest implements LinkedObject {
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
}
