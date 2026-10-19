package org.notima.util;

import java.time.LocalDate;

/**
 * Represents a tax id separated in parts.
 * 
 * <p>Swedish formats, shown for an organisationsnummer and a personnummer:</p>
 * <ul>
 *   <li>{@link #FMT_SE10}: {@code 5569543738}, {@code 8001011234}</li>
 *   <li>{@link #FMT_SE11}: {@code 556954-3738}, {@code 800101-1234} ({@code +} instead of
 *       {@code -} for a person who is 100 or older)</li>
 *   <li>{@link #FMT_SE12}: {@code 165569543738}, {@code 198001011234}</li>
 *   <li>{@link #FMT_SE13}: {@code 16556954-3738}, {@code 19800101-1234}</li>
 *   <li>{@link #FMT_SE14}: the VAT number, {@code SE556954373801}</li>
 * </ul>
 * 
 * <p>A personnummer (or samordningsnummer) is recognised by its first six digits being
 * a date; an organisationsnummer's month digits are 20 or more. The 12-digit forms prefix
 * an organisationsnummer with {@code 16}, the way Skatteverket expects it.</p>
 * 
 * @author Daniel Tamm
 *
 */
public class TaxIdStructure {

	// Available formats
	public static String FMT_UNKNOWN = "UNKNOWN";
	public static String FMT_SE14 = "SE14";
	public static String FMT_SE13 = "SE13";
	public static String FMT_SE12 = "SE12";
	public static String FMT_SE11 = "SE11";
	public static String FMT_SE10 = "SE10";
	
	protected String	prefix;
	protected String	leftmiddle;
	protected String	rightmiddle;
	protected String	suffix;
	/** True if the tax id was written with a {@code +}, i.e. the person is 100 or older. */
	protected boolean	plusSeparator;
	
	protected String	taxIdFormat;
	
	public TaxIdStructure(String taxIdFormat) {
		this.taxIdFormat = taxIdFormat;
	}
	
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getLeftmiddle() {
		return leftmiddle;
	}
	public void setLeftmiddle(String leftmiddle) {
		this.leftmiddle = leftmiddle;
	}
	public String getRightmiddle() {
		return rightmiddle;
	}
	public void setRightmiddle(String rightmiddle) {
		this.rightmiddle = rightmiddle;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public String getTaxIdFormat() {
		return taxIdFormat;
	}
	public void setTaxIdFormat(String taxIdFormat) {
		this.taxIdFormat = taxIdFormat;
	}
	public boolean isPlusSeparator() {
		return plusSeparator;
	}
	public void setPlusSeparator(boolean plusSeparator) {
		this.plusSeparator = plusSeparator;
	}
	
	/**
	 * Checks whether the tax id could be a valid date. Applies only 
	 * on Swedish tax ids. A samordningsnummer (day + 60) counts as a date.
	 * 
	 * @return			If the tax id (for physical persons) is a valid date.
	 */
	public boolean isValidDateSE() {
		int year = Integer.parseInt(prefix);
		int month = Integer.parseInt(leftmiddle);
		int day = Integer.parseInt(rightmiddle);
		
		if (year > 100 && 
			!(year > 1700 && year < 2300)) {
			return false;
		}
		
		if (month<1 || month>12)
			return false;
		
		if (day>60)
			day -= 60;		// samordningsnummer
		
		if (day<1 || day>31)
			return false;
		
		return true;
	}
	
	/**
	 * @return	True if this is a Swedish personnummer or samordningsnummer, false if it
	 * 			is an organisationsnummer.
	 */
	public boolean isPersonalNumberSE() {
		return isValidDateSE();
	}
	
	/**
	 * Prints the tax id in the given format, completing a missing century as of today.
	 * 
	 * @param dstFormat		The format use to print the tax id
	 * @return				The tax id in the given format.
	 * @see #printTaxId(String, LocalDate)
	 */
	public String printTaxId(String dstFormat) {
		return printTaxId(dstFormat, LocalDate.now());
	}
	
	/**
	 * Prints the tax id in the given format.
	 * 
	 * <p>Printing a personnummer given without century in a 12- or 13-digit format picks
	 * the century that makes the person between 0 and 99 years old in the year of
	 * {@code referenceDate}, or 100 and older if it was written with a {@code +}.
	 * Printing in {@link #FMT_SE11} uses {@code +} for a person 100 or older.</p>
	 * 
	 * @param dstFormat		The format use to print the tax id
	 * @param referenceDate	The date the century and age are determined from.
	 * @return				The tax id in the given format, or null if the format is unknown.
	 */
	public String printTaxId(String dstFormat, LocalDate referenceDate) {

		String yymmdd = prefix.substring(prefix.length()-2) + leftmiddle + rightmiddle;
		
		if (FMT_SE10.equalsIgnoreCase(dstFormat)) {
			return yymmdd + suffix;
		} else if (FMT_SE11.equalsIgnoreCase(dstFormat)) {
			boolean hundredOrOlder = isPersonalNumberSE()
					&& referenceDate.getYear() - Integer.parseInt(fullPrefix(referenceDate)) >= 100;
			return yymmdd + (hundredOrOlder ? "+" : "-") + suffix;
		} else if (FMT_SE12.equalsIgnoreCase(dstFormat)) {
			return fullPrefix(referenceDate) + leftmiddle + rightmiddle + suffix;
		} else if (FMT_SE13.equalsIgnoreCase(dstFormat)) {
			return fullPrefix(referenceDate) + leftmiddle + rightmiddle + "-" + suffix;
		} else if (FMT_SE14.equalsIgnoreCase(dstFormat)) {
			return "SE" + yymmdd + suffix + "01";
		}
		
		return null;
		
	}
	
	/**
	 * The year part with century: as given if it has four digits, {@code 16} followed
	 * by it for an organisationsnummer, otherwise the birth year of the person.
	 */
	private String fullPrefix(LocalDate referenceDate) {
		if (prefix.length()>=4)
			return prefix;
		if (!isPersonalNumberSE())
			return "16" + prefix;
		int referenceYear = referenceDate.getYear();
		int year = (referenceYear / 100) * 100 + Integer.parseInt(prefix);
		if (year > referenceYear)
			year -= 100;
		if (plusSeparator)
			year -= 100;
		return String.format("%04d", year);
	}
	
}
