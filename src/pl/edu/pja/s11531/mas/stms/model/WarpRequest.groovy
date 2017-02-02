package pl.edu.pja.s11531.mas.stms.model

import pl.edu.pja.s11531.mas.stms.persistence.ConstantsProvider
import pl.edu.pja.s11531.mas.stms.persistence.DatabaseObject
import pl.edu.pja.s11531.mas.stms.persistence.LinkedObject

import javax.validation.constraints.NotNull
import java.time.LocalDateTime

/**
 * Request for travel between star systems using hyperspace tunnels.
 */
class WarpRequest extends LinkedObject implements DatabaseObject {
    static final int SHIPS_FOR_DISCOUNT = Integer.parseInt(ConstantsProvider.SHIPS_FOR_DISCOUNT)

    Integer discount
    @NotNull
    Status status
    final List<LocalDateTime> warpDates = new ArrayList<>()

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
