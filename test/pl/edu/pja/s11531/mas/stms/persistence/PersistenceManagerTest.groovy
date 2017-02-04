package pl.edu.pja.s11531.mas.stms.persistence

import pl.edu.pja.s11531.mas.stms.ModelObjectFactory
import pl.edu.pja.s11531.mas.stms.model.Planet
import pl.edu.pja.s11531.mas.stms.model.Spaceship
import pl.edu.pja.s11531.mas.stms.model.Star
import pl.edu.pja.s11531.mas.stms.model.StarSystem
import spock.lang.Specification

class PersistenceManagerTest extends Specification {
    static final String TEST_CONFIG_FILE = 'res/test-config.json'
    static final String TEST_DATABASE_FILE = 'res/test-database.json'

    def setup() {
        def starSystem = ModelObjectFactory.starSystem()
        def star = ModelObjectFactory.star(starSystem)
        def planet = ModelObjectFactory.planet(starSystem)
        def spaceShip = ModelObjectFactory.spaceship()
    }

    def cleanup() {
        LinkedObject.clearExtent()
        new File(TEST_CONFIG_FILE).delete()
        new File(TEST_DATABASE_FILE).delete()
    }

    def "saves not empty files"() {
        when:
        PersistenceManager.saveConfig(TEST_CONFIG_FILE)
        PersistenceManager.saveDatabase(TEST_DATABASE_FILE)

        then:
        !getConfigContents().empty
        !getDatabaseContents().empty
    }

    def "saves different files"() {
        when:
        PersistenceManager.saveConfig(TEST_CONFIG_FILE)
        PersistenceManager.saveDatabase(TEST_DATABASE_FILE)

        then:
        getConfigContents() != getDatabaseContents()
    }

    def "loads extent"() {
        when:
        PersistenceManager.saveConfig(TEST_CONFIG_FILE)
        PersistenceManager.saveDatabase(TEST_DATABASE_FILE)
        LinkedObject.clearExtent()
        PersistenceManager.loadConfig(TEST_CONFIG_FILE)
        PersistenceManager.loadDatabase(TEST_DATABASE_FILE)

        then:
        !LinkedObject.getExtent(StarSystem.class).empty
        !LinkedObject.getExtent(Star.class).empty
        !LinkedObject.getExtent(Planet.class).empty
        !LinkedObject.getExtent(Spaceship.class).empty
    }

    private static def getConfigContents() {
        new File(TEST_CONFIG_FILE).readLines().join('\n')
    }

    private static def getDatabaseContents() {
        new File(TEST_DATABASE_FILE).readLines().join('\n')
    }


}
