package pl.edu.pja.s11531.mas.stms.model

import pl.edu.pja.s11531.mas.stms.constraints.CompositionCheck

import javax.validation.constraints.NotNull

/**
 * A building capable of creating hyperspace tunnel openings. Always linked in pairs.
 */
class WarpGate extends Building implements CompositionCheck {
    @NotNull
    WarpGate gate
    double maxObjectMass
    int pricePerTon
    transient boolean open

    void setGate(WarpGate gate) {
        checkComposition(this.gate, gate)
        this.gate = gate
        gate?.link(this, false)
    }

    @Override
    protected Map<Class, String> getLinkProperties() {
        return super.getLinkProperties() + [(WarpGate.class): 'gate']
    }
}
