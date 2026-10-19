package org.notima.util;

import java.time.LocalDate;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to parse and print tax id:s in different formats.
 * 
 * At the moment only Swedish tax id's are supported, see {@link TaxIdStructure} for the
 * formats. A tax id can be given in any of them, with or without blanks, and printed
 * in any other.
 * 
 * <pre>
 *   TaxIdFormatter.printTaxId(null, "800101-1234", TaxIdStructure.FMT_SE12)  // 198001011234
 *   TaxIdFormatter.printTaxId(null, "556954-3738", TaxIdStructure.FMT_SE12)  // 165569543738
 *   TaxIdFormatter.printTaxId(null, "SE556954373801", TaxIdStructure.FMT_SE11) // 556954-3738
 * </pre>
 * 
 * @author Daniel Tamm
 *
 */
public class TaxIdFormatter {

	private static Pattern	se10 = Pattern.compile("^(\\d{2})(\\d{2})(\\d{2})(\\d{4})$"); 
	private static Pattern	se11 = Pattern.compile("^(\\d{2})(\\d{2})(\\d{2})([\\-+])(\\d{4})$");
	private static Pattern	se12 = Pattern.compile("^(\\d{4})(\\d{2})(\\d{2})(\\d{4})$"); 
	private static Pattern	se13 = Pattern.compile("^(\\d{4})(\\d{2})(\\d{2})\\-(\\d{4})$"); 
	private static Pattern	se14 = Pattern.compile("^SE(\\d{2})(\\d{2})(\\d{2})(\\d{4})01$");

	/**
	 * Remove all non digit characters
	 *
	 * @param cleanUp		String to be cleaned.
	 * @return				A cleaned string.
	 */
	public static String toDigitsOnly(String cleanUp) {
		if (cleanUp==null) return("");
		StringBuffer buf = new StringBuffer();
		char c;
		for (int i=0; i<cleanUp.length();i++) {
			c = cleanUp.charAt(i);
			if (c>='0' && c<='9') {
				buf.append(c);
			}
		}
		return(buf.toString());
	}

    public static boolean hasDigitsOnly(String ref) {
        if (ref==null || ref.trim().length()==0) return(false);
        char c;
        for (int i=0; i<ref.length(); i++) {
            c = ref.charAt(i);
            if (!(c>='0' && c<='9')) {
                return(false);
            }
        }
        return true;
    }
	
    /**
     * Remove blanks from a string.
     * 
     * @param cleanUp		String to clean up.
     * @return				A string without blanks.
     */
    public static String removeBlanks(String cleanUp) {
    	if (cleanUp==null) return null;
    	StringBuffer result = new StringBuffer();
    	char c;
    	for (int i=0;i<cleanUp.length(); i++) {
    		c = cleanUp.charAt(i);
    		if (c==' ' || c=='\t' || c=='\n' || c=='\r')
    			continue;
    		result.append(c);
    	}
    	return result.toString();
    }
    
    /**
     * Print tax id in given format, completing a missing century as of today.
     * 
     * @param srcFormat		The format of the taxId-attribute. If null, the tax id format tries to be determined.
     * 						Note, this can be lengthy.
     * @param taxId			The actual tax id.
     * @param dstFormat		The destination format.
     * @return				The tax id in the destination format
     * @throws UnknownTaxIdFormatException 	if the dstFormat is not recognized.
     * @throws InvalidTaxIdFormatException 	If the tax ID can't be parsed.
     * @see #printTaxId(String, String, String, LocalDate)
     */
    public static String printTaxId(String srcFormat, String taxId, String dstFormat) throws UnknownTaxIdFormatException, InvalidTaxIdFormatException {
    	return printTaxId(srcFormat, taxId, dstFormat, LocalDate.now());
    }
    
    /**
     * Print tax id in given format.
     * 
     * @param srcFormat		The format of the taxId-attribute. If null, the tax id format tries to be determined.
     * @param taxId			The actual tax id.
     * @param dstFormat		The destination format.
     * @param referenceDate	The date a missing century of a personnummer is determined from,
     * 						see {@link TaxIdStructure#printTaxId(String, LocalDate)}.
     * @return				The tax id in the destination format
     * @throws UnknownTaxIdFormatException 	if the dstFormat is not recognized.
     * @throws InvalidTaxIdFormatException 	If the tax ID can't be parsed.
     */
    public static String printTaxId(String srcFormat, String taxId, String dstFormat, LocalDate referenceDate) throws UnknownTaxIdFormatException, InvalidTaxIdFormatException {
    	
    	if (dstFormat==null)
    		throw new UnknownTaxIdFormatException("Destination format can't be null.");
    	
    	String result = parseTaxId(srcFormat, taxId).printTaxId(dstFormat, referenceDate);
    	if (result==null)
    		throw new UnknownTaxIdFormatException("Destination format: " + dstFormat);
    	return result;
    	
    }
    
    /**
     * Parses a tax id into its parts, e.g. to find out whether it is a personnummer
     * ({@link TaxIdStructure#isPersonalNumberSE()}) before printing it.
     * 
     * @param srcFormat		The format of the tax id. If null, the format is determined.
     * @param taxId			The actual tax id. Blanks are ignored.
     * @return				The parsed tax id.
     * @throws UnknownTaxIdFormatException 	if the format is not recognized.
     * @throws InvalidTaxIdFormatException 	If the tax ID doesn't conform to the format.
     */
    public static TaxIdStructure parseTaxId(String srcFormat, String taxId) throws UnknownTaxIdFormatException, InvalidTaxIdFormatException {
    	
    	taxId = clean(taxId);
    	
    	if (srcFormat==null || srcFormat.trim().length()==0) {
    		srcFormat = determineFormat(null, taxId);
    	}
    	
    	if (srcFormat==null || srcFormat.equals(TaxIdStructure.FMT_UNKNOWN)) {
    		throw new UnknownTaxIdFormatException("Source format: " + srcFormat);
    	}
    	
    	Matcher m;
    	
    	if (TaxIdStructure.FMT_SE10.equalsIgnoreCase(srcFormat)) {
    		m = se10.matcher(taxId);
    	} else if (TaxIdStructure.FMT_SE11.equalsIgnoreCase(srcFormat)) {
    		m = se11.matcher(taxId);
    	} else if (TaxIdStructure.FMT_SE12.equalsIgnoreCase(srcFormat)) {
    		m = se12.matcher(taxId);
    	} else if (TaxIdStructure.FMT_SE13.equalsIgnoreCase(srcFormat)) {
    		m = se13.matcher(taxId);
    	} else if (TaxIdStructure.FMT_SE14.equalsIgnoreCase(srcFormat)) {
    		m = se14.matcher(taxId);
    	} else {
    		throw new UnknownTaxIdFormatException("Source format: " + srcFormat);
    	}

		if (!m.matches()) {
			throw new InvalidTaxIdFormatException("Taxid " + taxId + " doesn't conform to source format " + srcFormat + ".");
		}
		return createTaxIdStructureFromMatcher(m, srcFormat);
    	
    }
    
    /**
     * Reads the groups from a matching matcher into a tax id structure.
     * @param m
     * @return
     */
    private static TaxIdStructure createTaxIdStructureFromMatcher(Matcher m, String taxIdFormat) {

    	TaxIdStructure t = new TaxIdStructure(taxIdFormat);
    	t.setPrefix(m.group(1));
		t.setLeftmiddle(m.group(2));
		t.setRightmiddle(m.group(3));
		if (m.groupCount()==5) {		// with a - or + separator
			t.setPlusSeparator("+".equals(m.group(4)));
			t.setSuffix(m.group(5));
		} else {
			t.setSuffix(m.group(4));
		}
    	return t;
    	
    }
    
    /**
     * Determines tax id format by applying a number of regular expressions to the tax id.
     *
     * @param countryCode		If country code is supplied, only that country's tax id patterns are tried.
     * @param taxId				The tax id to determine.
     * @return					The tax id format. If it can't be matched, TaxIdStructure.FMT_UNKNOWN is returned.
     */
    public static String determineFormat(String countryCode, String taxId) {
    	
    	if (taxId==null)
    		return TaxIdStructure.FMT_UNKNOWN;
    	
    	taxId = clean(taxId);
    	
    	if (countryCode==null || countryCode.trim().length()==0 || "SE".equalsIgnoreCase(countryCode)) {
	    	Matcher m10 = se10.matcher(taxId);
	    	if (m10.matches())
	    		return TaxIdStructure.FMT_SE10;
	    	
	    	Matcher m11 = se11.matcher(taxId);
	    	if (m11.matches()) {
	    		return TaxIdStructure.FMT_SE11;
	    	}
	    	
	    	Matcher m12 = se12.matcher(taxId);
	    	if (m12.matches()) {
	    		return TaxIdStructure.FMT_SE12;
	    	}
	    	
	    	Matcher m13 = se13.matcher(taxId);
	    	if (m13.matches()) {
	    		return TaxIdStructure.FMT_SE13;
	    	}
	    	
	    	Matcher m14 = se14.matcher(taxId);
	    	if (m14.matches()) {
	    		return TaxIdStructure.FMT_SE14;
	    	}
    	}
    	
    	return TaxIdStructure.FMT_UNKNOWN;
    }
    
    /** Removes blanks and uppercases, so that e.g. {@code "se 556954 3738 01"} is recognised. */
    private static String clean(String taxId) {
    	return taxId==null ? "" : removeBlanks(taxId).replace("\u00a0", "").toUpperCase(Locale.ROOT);
    }
    
    
	
}
