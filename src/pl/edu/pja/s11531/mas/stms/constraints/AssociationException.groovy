package pl.edu.pja.s11531.mas.stms.constraints

import groovy.transform.InheritConstructors

/**
 * Thrown when there's association rule violation
 */
@InheritConstructors
class AssociationException extends ModelConstraintException {
    AssociationException(Object object1, Object object2) {
        super("There was a association violation between $object1 and $object2")
    }
}
