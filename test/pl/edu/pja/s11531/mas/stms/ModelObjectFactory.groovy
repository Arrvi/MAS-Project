package pl.edu.pja.s11531.mas.stms

import pl.edu.pja.s11531.mas.stms.datatypes.Distance
import pl.edu.pja.s11531.mas.stms.model.Planet
import pl.edu.pja.s11531.mas.stms.model.Star
import pl.edu.pja.s11531.mas.stms.model.StarSystem

import static pl.edu.pja.s11531.mas.stms.DataTypeFactory.*

/**
 * Creates dummy model objects
 */
class ModelObjectFactory {
    static StarSystem starSystem() {
        new StarSystem(trajectory: staticTrajectory(), name: "Test Star System")
    }

    static Star star(StarSystem starSystem) {
        new Star(starSystem: starSystem, trajectory: starSystem.trajectory, name: "Test star")
    }

    static Planet planet(StarSystem starSystem) {
        new Planet(
                starSystem: starSystem,
                trajectory: trajectory(starSystem.trajectory),
                name: "Test planet",
                radius: Distance.km(6371.0),
                mass: 1000000000000000000000,
                atmosphereCompounds: [oxygen(), nitrogen()]
        )
    }
}
