package pl.edu.pja.s11531.mas.stms.model

/**
 * Created by kris on 1/31/17.
 */
final class ConstantManager {
    private ConstantManager instance

    private ConstantManager() {

    }

    ConstantManager getInstance() {
        if (!instance) {
            instance = new ConstantManager()
        }
        return instance
    }

    static $static_propertyMissing(String name) {

    }
}
