package pl.edu.pja.s11531.mas.stms.constraints

import groovy.transform.InheritConstructors

/**
 * Thrown when state machine makes illegal transition between states.
 */
@InheritConstructors
class IllegalTransitionException extends ModelConstraintException {
    IllegalTransitionException(Object source, String originalState, String targetState) {
        super("$source tried to make illegal state transition: $originalState->$targetState")
    }
}
