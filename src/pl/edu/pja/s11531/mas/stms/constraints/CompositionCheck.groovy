package pl.edu.pja.s11531.mas.stms.constraints

import pl.edu.pja.s11531.mas.stms.persistence.LinkedObject

/**
 * Utility for checking composition links. Works on parts
 */
trait CompositionCheck {
    void checkComposition(LinkedObject oldPart, LinkedObject newPart) {
        if (oldPart != null && oldPart != newPart) {
            throw new CompositionException(this, oldPart, newPart)
        }
    }
}
