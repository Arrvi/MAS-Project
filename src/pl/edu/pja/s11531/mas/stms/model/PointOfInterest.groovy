package pl.edu.pja.s11531.mas.stms.model

import javax.validation.constraints.NotNull

/**
 * An important building in star system
 */
abstract class PointOfInterest extends Building {
    @NotNull
    LocationType locationType

    static enum LocationType {
        ON_PLANET,
        ON_MOON,
        ON_ORBIT
    }
}
