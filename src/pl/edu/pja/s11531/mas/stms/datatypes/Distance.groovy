package pl.edu.pja.s11531.mas.stms.datatypes

import java.math.RoundingMode

/**
 * Distance unit converters. Base value is in au
 */
class Distance {
    static final BigDecimal KM = 149597870700.0
    static final BigDecimal LY = (15813.0).setScale(9);

    static BigDecimal km(BigDecimal km) {
        return km.divide(KM, RoundingMode.HALF_UP)
    }

    static BigDecimal au(BigDecimal au) {
        return au
    }

    static BigDecimal ly(BigDecimal ly) {
        return ly.divide(LY, RoundingMode.HALF_UP)
    }
}
