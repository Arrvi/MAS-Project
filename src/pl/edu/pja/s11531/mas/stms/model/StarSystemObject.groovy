package pl.edu.pja.s11531.mas.stms.model

import pl.edu.pja.s11531.mas.stms.constraints.CompositionCheck

import javax.validation.constraints.NotNull

/**
 * Created by kris on 1/31/17.
 */
abstract class StarSystemObject extends SpaceObject implements CompositionCheck {
    @NotNull
    StarSystem starSystem

    void setStarSystem(StarSystem system) {
        checkComposition(starSystem, system)
        starSystem = system
        system?.link(this, false)
    }

    @Override
    protected Map<Class, String> getLinkProperties() {
        return super.getLinkProperties() + [(StarSystem.class): 'starSystem']
    }
}
