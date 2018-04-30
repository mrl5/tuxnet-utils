package com.tuxnet.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Rounds input double with given precision.
 * <p>
 * References:
 * <br>1) <a href="https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places">
 * https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places</a>
 */

public class DoubleRounder {
    /**
     * Rounds input double with given precision
     *
     * @param value  input number
     * @param places number of significant digits
     * @return rounded number
     */
    public Double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
