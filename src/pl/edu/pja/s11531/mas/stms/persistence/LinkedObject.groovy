package pl.edu.pja.s11531.mas.stms.persistence

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import pl.edu.pja.s11531.mas.stms.constraints.ModelConstraintException

import javax.validation.constraints.NotNull
import java.lang.reflect.Field

/**
 * Association and link support.
 */
@JsonIgnoreProperties(['id', 'linkProperties'])
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

    private static final Set<Integer> usedIds = [];

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

    protected Map<Class, String> getLinkProperties() {
        [:]
    }

    protected void linkInternal(LinkedObject object) {
        String propertyName = linkProperties[object.class]
        if (propertyName) {
            def prop = this.getProperty(propertyName)
            if (prop instanceof Collection) {
                prop.add(object)
            } else {
                setProperty(propertyName, object)
            }
        }
    }

    private boolean linkingLock = false

    void link(LinkedObject object, boolean linkBack = true) {
        if (linkingLock) return;
        linkingLock = true;
        linkInternal(object)
        if (linkBack) object.link(this, false)
        linkingLock = false;
    }

    String getId() {
        "${this.class.simpleName}.$_id"
    }

    void setId(String idString) {
        def segments = idString.split("\\.")
        if (segments[0] != this.class.simpleName) throw new IllegalArgumentException("Setting id of different class");
        def id = Integer.parseInt(segments[1])
        if (usedIds.contains(id)) {
            throw new ModelConstraintException("Unique id violation: $idString")
        }
        _id = id
        usedIds << id
    }

    JsonNode getJsonNode(JsonNodeFactory factory) {
        ObjectNode node = factory.objectNode()
        node.set 'class', factory.textNode(this.class.name)
        node.set 'id', factory.textNode(this.getId())
        node.set 'fields', getFieldsNode(factory)
        node.set 'links', getLinksNode(factory)
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    JsonNode getFieldsNode(JsonNodeFactory factory) {
        ObjectNode node = factory.objectNode()
        def ignoredProperties = getIgnoredProperties(this.class)
        this.getProperties()
                .findAll { String k, v -> !(k.matches("[A-Z][A-Z_]+") || ignoredProperties.contains(k)) }
                .findAll { k, v -> !(v instanceof Class || isLinkValue(v)) }
                .each { String k, Object v -> node.set k, factory.pojoNode(v) }
        return node
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    JsonNode getLinksNode(JsonNodeFactory factory) {
        ObjectNode node = factory.objectNode()
        def ignoredProperties = getIgnoredProperties(this.class)
        this.getProperties()
                .findAll { String k, v -> !ignoredProperties.contains(k) }
                .findAll { k, v -> isLinkValue(v) }
                .each { String k, v -> node.set k, getLinkNode(factory, v) }
        return node
    }

    JsonNode getLinkNode(JsonNodeFactory factory, Object obj) {
        if (obj instanceof LinkedObject) {
            return factory.textNode(obj.getId())
        }
        if (obj instanceof Collection) {
            ArrayNode node = factory.arrayNode()
            obj.each { node.add(getLinkNode(factory, it)) }
            return node
        }
        throw new IllegalArgumentException("Invalid link type");
    }

    protected static boolean isLinkValue(def obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof LinkedObject) {
            return true
        }
        if (obj instanceof Collection) {
            if (obj.size() == 0) return false;
            return isLinkValue(obj.iterator().next())
        }
        return false;
    }

    static ArrayNode serializeExtentToJson(Class targetClass = null) {
        def factory = new JsonNodeFactory(false)
        ArrayNode jsonExtent = factory.arrayNode()
        Set<String> ids = []
        new HashMap<Class<? extends LinkedObject>, List<LinkedObject>>(extent).each { k, v ->
            v.findAll { targetClass == null || targetClass.isInstance(it) }.findAll { !ids.contains(it.id) }.each {
                jsonExtent.add(it.getJsonNode(factory))
                ids << it.id
            }
        }

        return jsonExtent
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    static void unserializeJson(ArrayNode nodes, ObjectMapper mapper) {
        nodes.elements().each { ObjectNode node ->
            def className = (node.get('class') as TextNode).asText()
            def instance = LinkedObject.class.classLoader.loadClass(className)?.newInstance() as LinkedObject
            assert instance != null

            def fields = node.get('fields') as ObjectNode
            fields.fieldNames().findAll { it != 'id' }.each { String name ->
                Class fieldType = getPropertyType(instance.class, name)
                if (fieldType == null) {
                    throw new IllegalArgumentException("Cannot determine field's type: $name");
                }
                instance.setProperty(name, mapper.treeToValue(fields.get(name), fieldType))
            }
            instance.id = node.get('id').asText()
            println "Deserialized $instance.id"
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
                def link = links.get(name)
                if (link instanceof TextNode) {
                    instance.setProperty(name, instanceMap[link.asText()])
                    println "Linked $instance.id to ${instanceMap[link.asText()].id}"
                } else if (link instanceof ArrayNode) {
                    List<LinkedObject> instances = []
                    link.each { instances.add(instanceMap[it.asText()]) }
                    instance.setProperty(name, instances)
                    println "Linked $instance.id to ${instances*.id}"
                } else {
                    throw new IllegalArgumentException("Unsupported link format: ${link.class}")
                }
            }
        }
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

    @NotNull
    private static List<String> getIgnoredProperties(@NotNull Class cls) {
        if (cls == Object.class) return []
        List<String> props = []
        JsonIgnoreProperties annot =
                cls.declaredAnnotations.find { it instanceof JsonIgnoreProperties } as JsonIgnoreProperties
        if (annot != null) {
            props.addAll(annot.value())
        }
        return props + getIgnoredProperties(cls.superclass)
    }
}
