package pl.edu.pja.s11531.mas.stms.constraints

import groovy.transform.InheritConstructors

/**
 * Thrown when some constraint of the model is violated
 */
@InheritConstructors
class ModelConstraintException extends RuntimeException {
}
