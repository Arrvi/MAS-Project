package pl.edu.pja.s11531.mas.stms.persistence

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode

import java.lang.reflect.Field

/**
 * Association and link support.
 */
abstract class LinkedObject {
    /**
     * Map of object lists by their class. It defaults to empty list to avoid boilerplate checks while adding.
     * Note that each object is stored under its class key and all of its super classes. In other words class entry
     * contains all of its objects and objects of its subclass.
     */
    private static Map<Class<? extends LinkedObject>, List<LinkedObject>> extent = [:].withDefault { [] };

    private Map<Field, String> serializedLinks = [:]

    private static int lastId = 0;
    private int id

    /**
     * Base constructor. Adds object to extent.
     */
    LinkedObject() {
        id = ++lastId
        addObject this
    }

    /**
     * Adds an object to its class extent and all of its super classes' extents.
     */
    protected static <C extends LinkedObject> void addObject(C object) {
        Class cls = object.class
        while (cls && cls != LinkedObject.class) {
            extent[cls].add object
            cls = cls.superclass
        }
    }

    /**
     * @return extent of given class
     */
    static <C extends LinkedObject> List<C> getExtent(Class<C> cls) {
        return (List<C>) extent[cls];
    }

    /**
     * Clears extents of all classes
     */
    static void clearExtent() {
        extent.clear()
    }

    /**
     * Clears extent of given class
     */
    static void clearExtent(Class<? extends LinkedObject> cls) {
        extent.each {
            if (cls.isAssignableFrom(it.key) && it.key != cls) {
                it.value.clear()
            }
        }
        def classExtent = extent[cls]
        Class currentClass = cls.superclass
        while (currentClass && currentClass != LinkedObject.class) {
            extent[currentClass].removeAll classExtent
            currentClass = currentClass.superclass
        }
        extent[cls].clear()
    }

    String getId() {
        "${this.class.simpleName}.$id"
    }

    JsonNode getJsonNode(JsonNodeFactory factory) {
        ObjectNode node = factory.objectNode()
        node.set 'class', factory.textNode(this.class.name)
        node.set 'id', factory.textNode(this.getId())
        node.set 'fields', getFieldsNode(factory)
        node.set 'links', getLinksNode(factory)
    }

    JsonNode getFieldsNode(JsonNodeFactory factory) {
        ObjectNode node = factory.objectNode()
        this.getProperties()
                .findAll { k, v -> !(v instanceof LinkedObject || v instanceof Class) }
                .each { String k, Object v -> node.set k, factory.pojoNode(v) }
        return node
    }

    JsonNode getLinksNode(JsonNodeFactory factory) {
        ObjectNode node = factory.objectNode()
        this.getProperties()
                .findAll { k, v -> v instanceof LinkedObject }
                .each { String k, LinkedObject v -> node.set k, factory.textNode(v.getId()) }
        return node
    }

    static String serializeExtentToJson() {
        def factory = new JsonNodeFactory(false)
        ArrayNode jsonExtent = factory.arrayNode()
        extent.values().each { it.each { jsonExtent.add(it.getJsonNode(factory)) } }

        ObjectMapper mapper = new ObjectMapper()
        return mapper.writeValueAsString(jsonExtent)
    }
}
