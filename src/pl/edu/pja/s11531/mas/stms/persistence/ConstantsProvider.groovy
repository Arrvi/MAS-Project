package pl.edu.pja.s11531.mas.stms.persistence

/**
 * Global provider for constants. They are retrieved from static file.
 */
final class ConstantsProvider {
    private static final String PROPERTIES_PATH = "constants.properties"
    private static ConstantsProvider instance
    private Properties properties = new Properties()

    private ConstantsProvider() {
        File propertiesFile = new File(PROPERTIES_PATH)
        propertiesFile.withInputStream {
            properties.load(it)
        }
    }

    static ConstantsProvider getInstance() {
        if (!instance) {
            instance = new ConstantsProvider()
        }
        return instance
    }

    static $static_propertyMissing(String name) {
        instance.properties.getProperty(name)
    }
}
