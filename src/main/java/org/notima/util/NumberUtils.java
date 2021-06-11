package org.notima.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberUtils {

	/**
	 * Round to precision.
	 * 
	 * @param bd				The number to round.
	 * @param precision			The precision to round to
	 * @return		The rounded value.
	 */
	public static BigDecimal roundToPrecision(BigDecimal bd, int precision) {
		BigInteger bi = bd.movePointRight(precision).toBigInteger();
		bd = new BigDecimal(bi).movePointLeft(precision);
		return bd;
	}
	
	/**
	 * Round to precision.
	 * 
	 * @param 	d	The number to round.
	 * @param	precision	The precision to round to
	 * @return		The rounded value.
	 */
	public static double roundToPrecision(double d, int precision) {
		long roundedLong = Math.round(d * Math.pow(10, precision));
		double rounded = (roundedLong / Math.pow(10, precision));
		return rounded;
	}
	
	
	
}
