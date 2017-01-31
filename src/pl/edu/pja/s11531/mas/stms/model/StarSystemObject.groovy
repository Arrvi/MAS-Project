package pl.edu.pja.s11531.mas.stms.model

import javax.validation.constraints.NotNull

/**
 * Created by kris on 1/31/17.
 */
abstract class StarSystemObject extends SpaceObject {
    @NotNull
    StarSystem starSystem
}
