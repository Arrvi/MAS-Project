package pl.edu.pja.s11531.mas.stms.gui.util

import pl.edu.pja.s11531.mas.stms.persistence.LinkedObject

/**
 * Created by kris on 2/5/17.
 */
class LinkedObjectDecorator {
    LinkedObject object

    LinkedObjectDecorator(LinkedObject object) {
        this.object = object
    }

    @Override
    String toString() {
        return object.hasProperty('name') ? object.getProperty('name') : object.id
    }
}
