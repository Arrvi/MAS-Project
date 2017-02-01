package pl.edu.pja.s11531.mas.stms.persistence

import pl.edu.pja.s11531.mas.stms.ModelObjectFactory
import spock.lang.Specification

class LinkedObjectTest extends Specification {
    void cleanup() {
        LinkedObject.clearExtent()
    }

    def "SerializeExtentToJson"() {
        setup:
        def starSystem = ModelObjectFactory.starSystem()
        def star = ModelObjectFactory.star(starSystem)
        def planet = ModelObjectFactory.planet(starSystem)

        when:
        def json = LinkedObject.serializeExtentToJson()

        then:
        json != null
        !json.empty
    }
}
