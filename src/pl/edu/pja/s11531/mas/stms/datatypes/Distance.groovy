package pl.edu.pja.s11531.mas.stms.datatypes

import java.math.RoundingMode

/**
 * Distance unit converters. Base value is in au
 */
class Distance {
    /**
     * Kilometers in astronomical unit
     */
    static final BigDecimal KM = 149597870700.0
    /**
     * Light years in astronomical unit
     */
    static final BigDecimal LY = (15813.0).setScale(9);

    /**
     * Converts distance in kilometers to unit-less primitive value.
     * @param km Distance in kilometers
     * @return Distance in astronomical units
     */
    static BigDecimal km(BigDecimal km) {
        return km.divide(KM, RoundingMode.HALF_UP)
    }

    /**
     * Converts distance in astronomical unit to unit-less primitive value.
     * Since AU is primitive unit, this method is only for convention.
     * @param au Distance in astronomical unit
     * @return Distance in astronomical unit
     */
    static BigDecimal au(BigDecimal au) {
        return au
    }

    /**
     * Converts distance in light years to unit-less primitive value.
     * @param ly Distance in light years
     * @return Distance in astronomical unit
     */
    static BigDecimal ly(BigDecimal ly) {
        return ly.divide(LY, RoundingMode.HALF_UP)
    }
}
