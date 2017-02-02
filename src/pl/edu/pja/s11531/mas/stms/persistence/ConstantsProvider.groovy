package pl.edu.pja.s11531.mas.stms.persistence

/**
 * Global provider for constants. They are retrieved from static file.
 */
final class ConstantsProvider {
    private static final String PROPERTIES_PATH = "res/constants.properties"
    private static ConstantsProvider instance
    private Properties constants = new Properties()

    private ConstantsProvider() {
        File propertiesFile = new File(PROPERTIES_PATH)
        propertiesFile.withInputStream {
            constants.load(it)
        }
    }

    static ConstantsProvider getInstance() {
        if (!instance) {
            instance = new ConstantsProvider()
        }
        return instance
    }

    static $static_propertyMissing(String name) {
        getInstance().constants.get(name)
    }
}
