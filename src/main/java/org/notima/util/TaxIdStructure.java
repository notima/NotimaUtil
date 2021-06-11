package org.notima.util;

/**
 * Represents a tax id separated in parts.
 * 
 * @author Daniel Tamm
 *
 */
public class TaxIdStructure {

	// Available formats
	public static String FMT_UNKNOWN = "UNKNOWN";
	public static String FMT_SE14 = "SE14";
	public static String FMT_SE12 = "SE12";
	public static String FMT_SE11 = "SE11";
	public static String FMT_SE10 = "SE10";
	
	protected String	prefix;
	protected String	leftmiddle;
	protected String	rightmiddle;
	protected String	suffix;
	
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
	/**
	 * Checks whether the tax id could be a valid date. Applies only 
	 * on Swedish tax ids.
	 * 
	 * @return
	 */
	public boolean isValidDateSE() {
		int year = Integer.parseInt(prefix);
		int month = Integer.parseInt(leftmiddle);
		int day = Integer.parseInt(rightmiddle);
		
		if (year > 100 && 
			!(year > 1700 && year < 2300)) {
			return false;
		}
		
		if (month>13)
			return false;
		
		if (day>31)
			return false;
		
		return true;
	}
	
	/**
	 * 
	 * 
	 * @param dstFormat
	 * @return
	 */
	public String printTaxId(String dstFormat) {

		String result = null;
		
		if (FMT_SE10.equalsIgnoreCase(dstFormat)) {
			result = prefix.substring(prefix.length()-2, prefix.length()) + leftmiddle + rightmiddle + suffix;
		} else if (FMT_SE11.equalsIgnoreCase(dstFormat)) {
			result = prefix.substring(prefix.length()-2, prefix.length()) + leftmiddle + rightmiddle + "-" + suffix;
		} else if (FMT_SE12.equalsIgnoreCase(dstFormat)) {
			if (!FMT_SE12.equalsIgnoreCase(taxIdFormat) && prefix.length()<4) {
				if (isValidDateSE())
					prefix = "XX" + prefix;		// It's not possible to determine century if that information is missing (19 / 20)
				else
					prefix = "16" + prefix;
			}
			result = prefix + leftmiddle + rightmiddle + suffix;
		} else if (FMT_SE14.equalsIgnoreCase(dstFormat)) {
			result = "SE" + prefix + leftmiddle + rightmiddle + suffix + "01";
		}
		
		return result;
		
	}
	
}
