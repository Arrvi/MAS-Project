package pl.edu.pja.s11531.mas.stms.model

import javax.validation.constraints.NotNull

/**
 * A building capable of creating hyperspace tunnel openings. Always linked in pairs.
 */
class WarpGate extends Building {
    @NotNull
    WarpGate gate
    double maxObjectMass
    int pricePerTon
    transient boolean open
}
