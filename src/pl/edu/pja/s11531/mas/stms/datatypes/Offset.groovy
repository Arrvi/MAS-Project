package pl.edu.pja.s11531.mas.stms.datatypes

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

import javax.validation.constraints.NotNull

/**
 * Simple BigDecimal 3D vector
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class Offset implements DataType {
    @NotNull
    final BigDecimal x
    @NotNull
    final BigDecimal y
    @NotNull
    final BigDecimal z

    @JsonCreator
    Offset(
            @JsonProperty('x') @NotNull BigDecimal x,
            @JsonProperty('y') @NotNull BigDecimal y,
            @JsonProperty('z') @NotNull BigDecimal z) {
        this.x = x
        this.y = y
        this.z = z
    }

    Offset(@NotNull Offset offset) {
        this.x = offset.x
        this.y = offset.y
        this.z = offset.z
    }

    Offset plus(Offset pos) {
        return new Offset(x + pos.x, y + pos.y, z + pos.z)
    }

    Offset minus(Offset pos) {
        return new Offset(x - pos.x, y - pos.y, z - pos.z)
    }

    Offset multiply(BigDecimal multiplier) {
        return new Offset(x * multiplier, y * multiplier, z * multiplier)
    }

    Offset divide(BigDecimal divider) {
        return new Offset(x / divider, y / divider, z / divider)
    }

    BigDecimal getLength() {
        Math.sqrt([x, y, z]*.pow(2).sum() as double)
    }

    BigDecimal getDistanceTo(Offset pos) {
        (this - pos).length
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Offset offset = (Offset) o

        if (x != offset.x) return false
        if (y != offset.y) return false
        if (z != offset.z) return false

        return true
    }

    int hashCode() {
        int result
        result = (x != null ? x.hashCode() : 0)
        result = 31 * result + (y != null ? y.hashCode() : 0)
        result = 31 * result + (z != null ? z.hashCode() : 0)
        return result
    }
}
