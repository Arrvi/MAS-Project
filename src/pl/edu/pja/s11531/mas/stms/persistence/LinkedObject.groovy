package pl.edu.pja.s11531.mas.stms.persistence

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule

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

    private static boolean autoIncrementId = true;
    private static int lastId = 0;
    protected int _id

    /**
     * Base constructor. Adds object to extent.
     */
    LinkedObject() {
        if (autoIncrementId) {
            _id = ++lastId
        }
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
        "${this.class.simpleName}.$_id"
    }

    void setId(String id) {
        def segments = id.split("\\.")
        if (segments[0] != this.class.simpleName) throw new IllegalArgumentException("Setting id of different class");
        _id = Integer.parseInt(segments[1])
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
                .findAll { String k, v -> !k.matches("[A-Z][A-Z_]+") }
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

    static String serializeExtentToJson(Class targetClass = null) {
        def factory = new JsonNodeFactory(false)
        ArrayNode jsonExtent = factory.arrayNode()
        extent.values()
                .each
                {
                    it
                            .findAll { targetClass == null || targetClass.isInstance(it) }
                            .each { jsonExtent.add(it.getJsonNode(factory)) }
                }

        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
        return mapper.writeValueAsString(jsonExtent)
    }

    static void unserializeJson(String json) {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
        def nodes = mapper.readTree(json) as ArrayNode
        println "Tree read"


        nodes.elements().each { ObjectNode node ->
            println "Reading object: $node"
            def className = (node.get('class') as TextNode).asText()
            def instance = LinkedObject.class.classLoader.loadClass(className)?.newInstance() as LinkedObject
            assert instance != null

            println "Got instance of $className: $instance"

            def fields = node.get('fields') as ObjectNode
            fields.fieldNames().findAll { it != 'id' }.each { String name ->
                println "Loading field $name: ${fields.get(name)}"
                Class fieldType = getPropertyType(instance.class, name)
                if (fieldType == null) {
                    throw new IllegalArgumentException("Cannot determine field's type: $name");
                }
                instance.setProperty(name, mapper.treeToValue(fields.get(name), fieldType))
            }
            instance.id = node.get('id').asText()
        }

        Map<String, LinkedObject> instanceMap = [:]
        extent.values().each {
            it.each {
                instanceMap[it.getId()] = it
            }
        }

        nodes.elements().each { Object node ->
            String id = node.get('id').asText()
            def instance = instanceMap[id]
            def links = node.get('links') as ObjectNode
            links.fieldNames().each { String name ->
                instance.setProperty(name, instanceMap[links.get(name).asText()])
            }
        }

        println "Deserialized successfully: $extent"
    }

    private static Class getPropertyType(Class cls, String name) {
        if (cls == Object.class) return null;
        Field field = null
        try {
            field = cls.getDeclaredField(name)
        } catch (NoSuchFieldException ignored) {
        }
        if (field == null) {
            return getPropertyType(cls.superclass, name)
        }
        return field.type
    }
}
