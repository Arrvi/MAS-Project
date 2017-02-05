package pl.edu.pja.s11531.mas.stms.persistence

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import pl.edu.pja.s11531.mas.stms.ModelObjectFactory
import pl.edu.pja.s11531.mas.stms.model.ServiceShop
import pl.edu.pja.s11531.mas.stms.model.Spaceship
import pl.edu.pja.s11531.mas.stms.model.SpaceshipType
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
        def json = PersistenceManager.mapper.writeValueAsString(LinkedObject.serializeExtentToJson())

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
        json instanceof ArrayNode

        when:
        def spaceshipNode = json.find { it.get('class').asText().endsWith('Spaceship') } as ObjectNode
        def typeNode = json.find { it.get('class').asText().endsWith('SpaceshipType') } as ObjectNode

        then:
        spaceshipNode != null
        typeNode != null
        typeNode.get('id')?.asText() == spaceShip.type.id
        typeNode.get('fields')?.get('name')?.asText() == 'Test type'
        typeNode.get('links')?.size() == 0
        spaceshipNode.get('id')?.asText() == spaceShip.id
        spaceshipNode.get('fields')?.get('name')?.asText() == 'Test ship'
        spaceshipNode.get('fields')?.get('currentOwner')?.asText() == 'Test owner'
        spaceshipNode.get('fields')?.get('currentCaptain')?.asText() == 'Test captain'
        spaceshipNode.get('fields')?.get('mass')?.asText() == '1000.0'
        spaceshipNode.get('links')?.get('type')?.asText() == spaceShip.type.id
    }

    def "serialization can be separated"() {
        setup:
        def starSystem = ModelObjectFactory.starSystem()
        def star = ModelObjectFactory.star(starSystem)
        def planet = ModelObjectFactory.planet(starSystem)
        def spaceShip = ModelObjectFactory.spaceship()

        when:
        def json1 = PersistenceManager.mapper.writeValueAsString(LinkedObject.serializeExtentToJson(DatabaseObject.class))
        def json2 = PersistenceManager.mapper.writeValueAsString(LinkedObject.serializeExtentToJson(ConfigObject.class))

        then:
        json1 != null
        json2 != null
        json1 != json2
    }

    def "serialized object can be deserialized"() {
        setup:
        def starSystem = ModelObjectFactory.starSystem()
        def star = ModelObjectFactory.star(starSystem)
        def planet = ModelObjectFactory.planet(starSystem)
        def spaceShip = ModelObjectFactory.spaceship()

        when:
        def json = PersistenceManager.mapper.writeValueAsString(LinkedObject.serializeExtentToJson())
        LinkedObject.clearExtent()
        LinkedObject.unserializeJson(PersistenceManager.mapper.readTree(json) as ArrayNode, PersistenceManager.mapper)

        then:
        !LinkedObject.getExtent(Spaceship.class).empty
    }

    def "links"() {
        setup:
        def starSystem = ModelObjectFactory.starSystem()
        def star = ModelObjectFactory.star(null)

        when:
        starSystem.link(star)

        then:
        star.starSystem == starSystem
    }

    def "serialized collection links can be deserialized"() {
        setup:
        def serviceShop = ModelObjectFactory.serviceShop()

        when:
        def json = PersistenceManager.mapper.writeValueAsString(LinkedObject.serializeExtentToJson())
        LinkedObject.clearExtent()
        LinkedObject.unserializeJson(PersistenceManager.mapper.readTree(json) as ArrayNode, PersistenceManager.mapper)

        then:
        !LinkedObject.getExtent(ServiceShop.class).empty
        !LinkedObject.getExtent(SpaceshipType.class).empty
    }
}
