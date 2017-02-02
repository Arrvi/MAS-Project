package pl.edu.pja.s11531.mas.stms.persistence

import pl.edu.pja.s11531.mas.stms.ModelObjectFactory
import spock.lang.Specification

class LinkedObjectTest extends Specification {
    void cleanup() {
        LinkedObject.clearExtent()
    }

    def "serialization creates json"() {
        setup:
        def starSystem = ModelObjectFactory.starSystem()
        def star = ModelObjectFactory.star(starSystem)
        def planet = ModelObjectFactory.planet(starSystem)
        def spaceShip = ModelObjectFactory.spaceship()

        when:
        def json = LinkedObject.serializeExtentToJson()

        then:
        json != null
        !json.empty
        json != '[]'
        println json
    }

    def "serialization creates correct json"() {
        setup:
        def spaceShip = ModelObjectFactory.spaceship()

        when:
        def json = LinkedObject.serializeExtentToJson()

        then:
        json == '[{' +
                '"class":"pl.edu.pja.s11531.mas.stms.model.SpaceshipType",' +
                '"id":"' + spaceShip.type.id + '",' +
                '"fields":{' +
                '"id":"' + spaceShip.type.id + '",' +
                '"name":"Test type"},' +
                '"links":{}},' +
                '{"class":"pl.edu.pja.s11531.mas.stms.model.Spaceship",' +
                '"id":"' + spaceShip.id + '",' +
                '"fields":{' +
                '"currentCaptain":"Test captain",' +
                '"currentOwner":"Test owner",' +
                '"id":"' + spaceShip.id + '",' +
                '"SPEED":10.0,' +
                '"name":"Test ship",' +
                '"mass":1000.0},' +
                '"links":{"type":"' + spaceShip.type.id + '"}}]'
        println json
    }

    def "serialization can be separated"() {
        setup:
        def starSystem = ModelObjectFactory.starSystem()
        def star = ModelObjectFactory.star(starSystem)
        def planet = ModelObjectFactory.planet(starSystem)
        def spaceShip = ModelObjectFactory.spaceship()

        when:
        def json1 = LinkedObject.serializeExtentToJson(DatabaseObject.class)
        def json2 = LinkedObject.serializeExtentToJson(ConfigObject.class)

        then:
        json1 != null
        json2 != null
        json1 != json2
    }
}
