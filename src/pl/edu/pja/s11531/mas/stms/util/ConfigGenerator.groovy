package pl.edu.pja.s11531.mas.stms.util

import pl.edu.pja.s11531.mas.stms.datatypes.*
import pl.edu.pja.s11531.mas.stms.model.*
import pl.edu.pja.s11531.mas.stms.persistence.LinkedObject
import pl.edu.pja.s11531.mas.stms.persistence.PersistenceManager

import java.time.LocalDateTime
import java.time.Period

/**
 * Created by kris on 2/5/17.
 */
class ConfigGenerator {
    public static final int ORBIT_STEPS = 20
    String[] systemNames = ['Alpha', 'Beta', 'Gamma', 'Delta', 'Epsilon', 'Zeta', 'Eta', 'Theta', 'Iota', 'Kappa',
                            'Lambda', 'Mu', 'Nu', 'Xi', 'Omicron', 'Pi', 'Rho', 'Sigma', 'Tau', 'Upsilon', 'Phi',
                            'Chi', 'Psi', 'Omega']
    String[] gasNames = ['hydrogen', 'nitrogen', 'oxygen', 'water vapour', 'carbon oxide', 'carbon dioxide', 'helium',
                         'methane', 'ethane', 'fluorine', 'neon', 'chlorine', 'argon', 'krypton', 'sulfur dioxide',
                         'sulfuric acid vapour', 'hydrogen chloride']
    String[] shipTypes = ['Guardian class', 'Pandora miner', 'Conquistador tanker', 'Celina vessel', 'Cain tanker',
                          'Marchana miner', 'Frenzy vessel', 'Infinitum class', 'Gremlin vessel', 'Vagabond vessel',
                          'Katherina cruiser', 'Actium class', 'Verdant class', 'Hurricane bomber', 'Spitfire figher']

    int systems = 10
    int maxPlanets = 10
    boolean generatePOI = true
    double tunnelProb = 0.7
    int minPrice = 10
    int maxPrice = 100
    BigDecimal universeSize = Distance.ly(1000000.0)

    Random random = new Random()

    double radiationSourceProb = 0.7
    double atmoCompProbBase = 0.2

    public static void main(String[] args) {
        new ConfigGenerator().generate()
    }

    void generate() {
        println "Begin"

        for (String type in shipTypes) {
            new SpaceshipType(name: type)
        }

        for (int s = 0; s < systems; s++) {
            println "Generating system $s"
            def system = new StarSystem(name: generateSystemName(s), trajectory: generateStaticTrajectory())

            println "Generating star"
            system.link(new Star(
                    name: system.name,
                    trajectory: system.trajectory,
                    mass: random.nextDouble() * 1000000000 + 1000000000,
                    radiation: generateRadiation().collectEntries { [it.type, it] }
            ))

            for (int p = random.nextInt(maxPlanets + 1); p > 0; p--) {
                println "Generating planet $s:$p"

                BigDecimal radius = new BigDecimal(random.nextDouble() + '')

                system.link(
                        new Planet(
                                name: generatePlanetName(system.name, p),
                                mass: 1000000 * random.nextDouble() + 1000,
                                radius: Math.random()**4 * 100000,
                                trajectory: generateOrbit(system.trajectory, radius, ORBIT_STEPS),
                                atmosphereCompounds: generateAtmohpere()
                        )
                )
                if (generatePOI) {
                    for (int i = random.nextInt(5); i > 0; i--) {
                        if (random.nextDouble() < 0.5) {
                            println "Generating service shop"
                            system.link(new ServiceShop(supportedShipTypes: generateRandomShipTypes()))
                        } else {
                            println "Generating inn"
                            system.link(new Inn(minimalPrice: random.nextInt(100), rating: random.nextDouble() * 10))
                        }
                    }
                }
            }
        }

        println "Generating tunnels"

        def systems = new LinkedList(LinkedObject.getExtent(StarSystem.class))
        def systemsPrime = new LinkedList(systems)
        systems.each { StarSystem sysA ->
            systemsPrime.remove(sysA)
            systemsPrime.each { StarSystem sysB ->
                if (random.nextDouble() < tunnelProb) {
                    println "Generating tunnel between $sysA.name and $sysB.name"
                    def maxObjMass = random.nextDouble()**2 * 1000000 + 1000
                    def gateA = new WarpGate(
                            name: "$sysA -> $sysB",
                            trajectory: generateOrbit(sysA.trajectory, Distance.au(random.nextDouble()**2 * 100), 10),
                            maxObjectMass: maxObjMass,
                            pricePerTon: random.nextDouble() * (maxPrice - minPrice) + minPrice
                    )
                    def gateB = new WarpGate(
                            name: "$sysB -> $sysA",
                            trajectory: generateOrbit(sysA.trajectory, Distance.au(random.nextDouble()**2 * 100), 10),
                            maxObjectMass: maxObjMass,
                            pricePerTon: random.nextDouble() * (maxPrice - minPrice) + minPrice
                    )
                    gateA.link(gateB)
                    sysA.link(gateA)
                    sysB.link(gateB)
                }
            }
        }

        println "Saving config extent"
        PersistenceManager.saveConfig()
        println "Done."
    }

    Trajectory generateStaticTrajectory() {
        new Trajectory(TimedPath.stationary(generateRandomPosition()), null)
    }

    Position generateRandomPosition() {
        return new Position(generateRandomOffset(universeSize), LocalDateTime.now())
    }

    Offset generateRandomOffset(BigDecimal boundSize) {
        return new Offset(
                boundSize * new BigDecimal(random.nextDouble()),
                boundSize * new BigDecimal(random.nextDouble()),
                boundSize * new BigDecimal(random.nextDouble()))
    }

    Trajectory generateOrbit(Trajectory parent, BigDecimal radius, int steps) {
        return new Trajectory(
                new TimedPath(
                        generateOrbitPath(radius, steps),
                        generateRandomDate(),
                        Period.ofDays((radius * 365).intValue())),
                parent)
    }

    Path generateOrbitPath(BigDecimal radius, int steps) {
        List<Offset> offsets = []
        for (double d = 0; d < Math.PI * 2; d += Math.PI * 2 / steps) {
            offsets << new Offset(radius * Math.cos(d), radius * Math.sin(d), 0.0)
        }
        return new Path(offsets)
    }

    LocalDateTime generateRandomDate() {
        return LocalDateTime.of(
                1000 + random.nextInt(1000),
                random.nextInt(12) + 1,
                random.nextInt(28) + 1,
                0, 0
        )
    }

    String generateSystemName(int index) {
        return systemNames[index]
    }

    static String generatePlanetName(String systemName, int index) {
        return "$systemName $index"
    }

    Set<RadiationSource> generateRadiation() {
        Set<RadiationSource> sources = []
        for (t in Radiation.Type.values()) {
            if (random.nextDouble() < radiationSourceProb)
                sources << generateRandomRadiationSource(t)
        }
        return sources
    }

    RadiationSource generateRandomRadiationSource(Radiation.Type type) {
        new RadiationSource(Radiation.FATAL_INTENSITY * 10 * new BigDecimal(random.nextDouble()), type)
    }

    Set<AtmosphereCompound> generateAtmohpere() {
        Set<AtmosphereCompound> compounds = []
        for (int i = 0; i < gasNames.length; i++) {
            if (random.nextDouble() < atmoCompProbBase / (i + 1)) {
                compounds << generateAtmosphereCompound(gasNames[i])
            }
        }
        return compounds
    }

    AtmosphereCompound generateAtmosphereCompound(String name) {
        new AtmosphereCompound(name, random.nextDouble()**4 * 10000)
    }

    Set<SpaceshipType> generateRandomShipTypes() {
        Set<SpaceshipType> types = []
        int amount = random.nextInt(10) + 1
        int guard = 1000;
        def allTypes = LinkedObject.getExtent(SpaceshipType.class)
        while (guard-- > 0 && types.size() < amount) {
            types << allTypes[random.nextInt(allTypes.size())]
        }
        return types
    }
}
