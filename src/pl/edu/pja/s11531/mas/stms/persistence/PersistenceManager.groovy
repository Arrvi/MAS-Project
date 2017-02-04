package pl.edu.pja.s11531.mas.stms.persistence

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.json.JSONArray

/**
 * Saves and loads model extents to proper files with correct format.
 */
class PersistenceManager {
    static final String CONFIG_FILE = "res/config.json"
    static final String DATABASE_FILE = "res/database.json"
    static final boolean PRETTY_JSON_FILES = ConstantsProvider.PRETTY_JSON_FILES == 'true'
    static final int JSON_INDENT = 2

    private static ObjectMapper _mapper = null

    static ObjectMapper getMapper() {
        if (!_mapper) {
            _mapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
        }
        return _mapper
    }

    static void saveConfig(String filename = null) {
        save(ConfigObject.class, filename ?: CONFIG_FILE)
    }

    static void saveDatabase(String filename = null) {
        save(DatabaseObject.class, filename ?: DATABASE_FILE)
    }

    static void loadConfig(String filename = null) {
        load(filename ?: CONFIG_FILE)
    }

    static void loadDatabase(String filename = null) {
        load(filename ?: DATABASE_FILE)
    }

    private static void save(Class cls, String filename) {
        File outputFile = new File(filename);
        if (!outputFile.canWrite()) {
            outputFile.createNewFile()
            if (!outputFile.canWrite()) {
                throw new IOException("Cannot write to output file: $filename");
            }
        }

        def node = LinkedObject.serializeExtentToJson(cls)
        if (PRETTY_JSON_FILES) {
            def json = mapper.writeValueAsString(node)
            json = new JSONArray(json).toString(JSON_INDENT)
            outputFile.withDataOutputStream {
                it.writeBytes(json)
            }
        } else {
            outputFile.withDataOutputStream {
                mapper.writeValue(it, node)
            }
        }

    }

    private static void load(String filename) {
        File inputFile = new File(filename)
        if (!inputFile.canRead()) {
            throw new IOException("Cannot read from input file: $filename");
        }

        JsonNode node = null
        inputFile.withInputStream {
            node = mapper.readTree(it)
        }
        if (node == null) {
            throw new IOException("Cannot create node from file: $filename")
        }
        LinkedObject.unserializeJson(node as ArrayNode, mapper)
    }

}
