package pl.edu.pja.s11531.mas.stms.constraints

import groovy.transform.InheritConstructors

/**
 * Thrown when some object is violating composition constraint
 */
@InheritConstructors
class CompositionException extends AssociationException {
    CompositionException(Object owner, Object oldPart, Object newPart) {
        super("Composition violation: owner=$owner, part=$oldPart->$newPart")
    }
}
