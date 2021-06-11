package org.notima.util;

/**
 * Thrown if a tax id doesn't conform to the format
 * 
 * @author Daniel Tamm
 *
 */
public class InvalidTaxIdFormatException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9101032631801818427L;

	public InvalidTaxIdFormatException(String srcFormat) {
		super(srcFormat);
	}
	
}
