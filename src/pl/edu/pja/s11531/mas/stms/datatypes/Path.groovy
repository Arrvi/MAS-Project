package pl.edu.pja.s11531.mas.stms.datatypes

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

import javax.validation.constraints.NotNull

/**
 * Some path in space.
 *
 * For simplicity composed of points connected with straight lines
 */
@JsonIgnoreProperties(['length'])
class Path implements DataType {
    /**
     * Points defining this path. Unmodifiable
     */
    final List<Offset> points
    /**
     * Cached length of this path
     */
    final BigDecimal length

    private final List<SegmentPosition> segments

    @JsonCreator
    Path(@JsonProperty('points') @NotNull List<Offset> points) {
        if (points.size() < 2) throw new IllegalArgumentException("Path consists of at least 2 points");

        this.points = Collections.unmodifiableList(points)
        BigDecimal distanceSum = 0.0
        Offset lastPoint = null
        List<SegmentPosition> segments = new ArrayList<>()
        for (Offset point : points) {
            if (lastPoint) {
                segments << new SegmentPosition(new Segment(lastPoint, point), distanceSum)
                def distance = point.getDistanceTo(lastPoint)
                distanceSum += distance
            }
            lastPoint = point
        }
        this.length = distanceSum
        this.segments = Collections.unmodifiableList(segments)
    }

    /**
     * Creates path from list of coordinates.
     * @param coords coordinates in groups of 3 (x,y,z,x,y,z,x,y,z...)
     * @return new path
     */
    static Path of(List<BigDecimal>... coords) {
        new Path(coords.collect { new Offset(it[0], it[1], it[2]) })
    }

    /**
     * Calculates point on the path according to parameter t
     * @param t interpolation param; 0 <= t <= 1
     * @return interpolated point
     */
    Offset getPointAt(double t) {
        if (t == 0.0d || length == 0.0) {
            return points[0]
        }
        def desiredLength = t * length
        int segmentIndex = Collections.binarySearch(segments, desiredLength)
        if (segmentIndex < 0) {
            segmentIndex = -(segmentIndex + 1)
        }
        SegmentPosition position = segments.get(segmentIndex)
        return position.segment.interpolate((desiredLength - position.position) / position.segment.length)
    }

    private static final class SegmentPosition implements Serializable, Comparable<Double> {
        final Segment segment
        final double position

        SegmentPosition(Segment segment, double position) {
            this.segment = segment
            this.position = position
        }

        @Override
        int compareTo(Double t) {
            position > t ? -1 : position < t ? 1 : 0
        }
    }

    private static final class Segment implements Serializable {
        final Offset a
        final Offset b

        Segment(Offset a, Offset b) {
            this.a = a
            this.b = b
        }

        Offset interpolate(double t) {
            a * (1 - t) + b * t
        }

        double getLength() {
            a.getDistanceTo(b)
        }
    }
}
