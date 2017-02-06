package pl.edu.pja.s11531.mas.stms.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import pl.edu.pja.s11531.mas.stms.constraints.IllegalTransitionException
import pl.edu.pja.s11531.mas.stms.constraints.ModelConstraintException
import pl.edu.pja.s11531.mas.stms.persistence.ConstantsProvider
import pl.edu.pja.s11531.mas.stms.persistence.DatabaseObject
import pl.edu.pja.s11531.mas.stms.persistence.LinkedObject

import javax.validation.constraints.NotNull
import java.time.LocalDateTime

/**
 * Request for travel between star systems using hyperspace tunnels.
 */
@JsonIgnoreProperties(['ships', 'shipRequests'])
class WarpRequest extends LinkedObject implements DatabaseObject {
    static final int SHIPS_FOR_DISCOUNT = Integer.parseInt(ConstantsProvider.SHIPS_FOR_DISCOUNT)

    @NotNull
    final List<WarpGate> gates = new ArrayList<>()

    @NotNull
    final List<Spaceship> ships = new ArrayList<>()

    @NotNull
    final List<SpaceshipWarpRequest> shipRequests = new ArrayList<>()

    Integer discount
    @NotNull
    Status status
    @NotNull
    final List<LocalDateTime> warpDates = new ArrayList<>()

    void setGates(List<WarpGate> newGates) {
        gates.clear()
        gates.addAll(newGates)
        gates*.link(this, false)
    }

    void setWarpDates(List<LocalDateTime> newDates) {
        warpDates.clear()
        warpDates.addAll(newDates)
    }

    void setShips(List<Spaceship> newShips) {
        ships.clear()
        ships.addAll(newShips)
    }

    void setShipRequest(List<SpaceshipWarpRequest> requests) {
        shipRequests.clear()
        shipRequests.addAll(requests)
    }

    void setStatus(Status newStatus) {
        if (status != null) {
            if (newStatus == null) {
                throw new ModelConstraintException("Cannot set status to null")
            }
            if (!status.transitions.contains(newStatus)) {
                throw new IllegalTransitionException(this, status.toString(), newStatus.toString())
            }
        }
        status = newStatus
    }

    @Override
    protected Map<Class, String> getLinkProperties() {
        return super.getLinkProperties() + [
                (WarpGate.class)            : 'gates',
                (Spaceship.class)           : 'ships',
                (SpaceshipWarpRequest.class): 'shipRequests'
        ]
    }

    static enum Status {
        RECEIVED(ACCEPTED, DECLINED),
        ACCEPTED(CANCELLED, PAID),
        DECLINED(),
        DONE(),
        CANCELLED(),
        PAID(PENDING, CANCELLED),
        PENDING(INT_RECEIVED, DONE),
        INT_RECEIVED(INT_REPORTED),
        INT_REPORTED(CANCELLED, PENDING);

        final Set<Status> transitions;

        Status(Status... transitions) {
            this.transitions = Collections.unmodifiableSet(new HashSet(Arrays.asList(transitions)))
        }
    }
}
