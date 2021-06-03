/** ===================================================================
	Bankgiro Java API
    
    Copyright (C) 2009  Daniel Tamm
						Notima Consulting Group Ltd
						
	Copyright (C) 2019  Notima System Integration AB

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.						

    =================================================================== */


package org.notima.util;

import java.util.Calendar;


/**
 *
 * @author Daniel Tamm
 */
public class NotimaUtil {
	
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
     * Remove blanks from a string. Good for purging for instance IBAN numbers
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
    
    
	public static String trimLeadingZeros(String cleanUp) {
		if (cleanUp==null) return("");
		cleanUp = cleanUp.trim();
		if (cleanUp.length()==0) return("");
		int i=0;
		while(i<cleanUp.length() && (cleanUp.charAt(i))=='0') {
			i++;
		}
		return(cleanUp.substring(i));
	}
	
	/**
	 * Perform very basic validation (length checks)
	 * 
	 * @param swift			Swift to be validated.
	 * @param iban			Iban to be validated.
	 * @return	True if the IBAN passes a basic validation
	 */
	public static boolean validateIban(String swift, String iban) {
		boolean pass = false;
		if (swift!=null && swift.trim().length()>0 &&
				iban!=null && iban.trim().length()>0) {
			pass = true;
		}
		return(pass);	
	}
	
	/**
	 *  Validate Bankgiro
	 *  @param Bankgiro			A string containing the bankgiro to be validated.
	 *  @return true if the bankgiro has passed length test.
	 */
	public static boolean validateBankgiro (String Bankgiro)
	{
		int length = NotimaUtil.toDigitsOnly(Bankgiro).length();
		if (length > 0 && length < 10)
			return true;
		return false;
	}   //  validateBankgiro

	
	
	/**
	 * Converts a double to 12 digits where the two last digits
	 * are "öre" or "cents".
	 * Negative amounts are returned as absolute (no negative indicator)
	 * @param amount	The amount to be formatted.
	 * @return		A string representation of an amount in BG-format.
	 */
	public static String getAmountStr(double amount) {
		return(getAmountStr(amount, 12, true));
	}
	
	/**
	 * 
	 * @param amount	The amount to be converted.
	 * @param len		Let you specify the length of the string
	 * @param absolute	If true no negative indicator. If false the last digit is replaced
	 * 					with a letter according to a specific pattern to indicate a negative
	 * 					value.
	 * @return			The amount in string representation
	 */
	public static String getAmountStr(double amount, int len, boolean absolute) {
		StringBuffer buf = new StringBuffer();
		long newAmount = Math.round(Math.abs(amount)*100.0);
		buf.append(Long.toString(newAmount));
		while(buf.length()<len) {
			buf.insert(0, "0");
		}
		// Replace if negative and not absolute
		if (amount<0 && !absolute) {
			String minusStr;
			char lastDigit = buf.charAt(buf.length()-1);
			switch (lastDigit) {
				case '0' : minusStr = "-"; break;
				case '1' : minusStr = "J"; break;
				case '2' : minusStr = "K"; break;
				case '3' : minusStr = "L"; break;
				case '4' : minusStr = "M"; break;
				case '5' : minusStr = "N"; break;
				case '6' : minusStr = "O"; break;
				case '7' : minusStr = "P"; break;
				case '8' : minusStr = "Q"; break;
				case '9' : minusStr = "R"; break;
				default : minusStr = "E"; break; // Illegal character, shouldn't happen
			}
			// Replace last digit
			buf.replace(buf.length()-1, buf.length(), minusStr);
		}
		return(buf.toString());
	}
	
	public static double parseAmountStr(String amountStr) {
		Double amount = Double.parseDouble(amountStr);
		amount = amount / 100.0;
		return(amount);
	}

	/**
	 * Return Luhn Digit using an amount as in data
	 * 
	 * @param amount		The amount
	 * @return				The luhn digit associated with the amount.
	 */
	public static int getLuhnDigit(double amount) {
		String amountStr = trimLeadingZeros(getAmountStr(amount,20,true));
		return(getLuhnDigit(amountStr));
	}
	
	/**
	 * Luhn algoritm.
	 * @param indata		The numbers to calculated a Luhn digit from.
	 * @return	The luhn digit.
	 */
	public static int getLuhnDigit(String indata) {
		int a = 2;
		int sum = 0;
		int term;
		for (int i = indata.length() - 1; i >= 0; i--) {
		  term = Character.digit(indata.charAt(i),10) * a;
		  if ( term > 9) term -= 9;
		  sum += term;
		  a = 3 - a;
		}
		int tens = sum/10*10;
		int subtractfrom = tens+10;
		return((subtractfrom - sum)%10);
	}

	/**
	 * Converts a string of digits to the equivalent OCR-number
	 * @param indata		The data to be converted
	 * @return				An OCR number with a checkdigit.
	 */
	public static String toOCRNumber(String indata) {
		indata = toDigitsOnly(indata);
		return(indata + getLuhnDigit(indata));
    }
	
	/**
	 * Converts a string of digits to an OCR-number with length check
	 * @param indata		Numbers to be converted.
	 * @return				OCR-number with luhn digit and length check.
	 */
	public static String toOCRNumberWithLengthCheck(String indata) {
		indata = toDigitsOnly(indata);
		int length = indata.length()+2; // Add two for the check digits
		length = length%10; // Modulus
		String ocrCode = indata + length;
		return(ocrCode + getLuhnDigit(ocrCode));
	}
	
	/**
	 *  Validate OCR number.
	 *  - Based on LUHN formula (Modulus10)
	 *  @param OCR	The OCR-number to validate
	 *  @return True if OCR is correct
	 */
	public static boolean isValidOCRNumber (String OCR)
	{
		if (OCR == null || OCR.length() == 0)
			return false;

		/**
		 *  1:  Double the value of alternate digits beginning with
		 *      the	first right-hand digit (low order).
		 *  2:  Add the individual digits comprising the products
		 *      obtained in step 1 to each of the unaffected digits
		 *      in the original number.
		 *  3:  Subtract the total obtained in step 2 from the next higher
		 *      number ending in 0 [this in the equivalent of calculating
		 *      the "tens complement" of the low order digit (unit digit)
		 *      of the total].
		 *      If the total obtained in step 2 is a number ending in zero
		 *      (30, 40 etc.), the check digit is 0.
		 *  Example:
		 *  Account number: 4992 73 9871 6
		 *
		 *  4  9  9  2  7  3  9  8  7  1  6
		 *    x2    x2    x2    x2    x2
		 *  -------------------------------
		 *  4 18  9  4  7  6  9 16  7  2  6
		 *
		 *  4 + 1 + 8 + 9 + 4 + 7 + 6 + 9 + 1 + 6 + 7 + 2 + 6 = 70
		 *  70 % 10 = 0
		 */

		//  Clean up number
		String ccNumber1 = toDigitsOnly(OCR);
		int ccLength = ccNumber1.length();
		//  Reverse string
		StringBuffer buf = new StringBuffer();
		for (int i = ccLength; i != 0; i--)
			buf.append(ccNumber1.charAt(i-1));
		String ccNumber = buf.toString();

		int sum = 0;
		for (int i = 0; i < ccLength; i++)
		{
			int digit = Character.getNumericValue(ccNumber.charAt(i));
			if (i % 2 == 1)
			{
				digit *= 2;
				if (digit > 9)
					digit -= 9;
			}
			sum += digit;
		}

		if (sum % 10 == 0)
			return true;

		return false;
	}   //  validateOCRNumber

	/**
	 * Fills to a specified length
	 * 
	 * @param		str			The string to be filled. If null it will be filled only with fillChar.
	 * @param		rightAlign	If the string should be right aligned.
	 * @param		fillChar	The char to fill.
	 * @param		len			Lenght to fill. If str is more than len, str is truncated.
	 * @return	A string filled ackording to the given parameters.
	 */
	public static String fillToLength(String str, boolean rightAlign, char fillChar, int len) {
		if (str==null) str = "";
		StringBuffer buf = new StringBuffer();
		if (str.length()>len) {
			if (rightAlign)
				buf.append(str.substring(str.length()-len, str.length()));
			else
				buf.append(str.substring(0, len));
			return(buf.toString());
		}
		// Append
		buf.append(str);
		while(buf.length()<len) {
			if (rightAlign) {
				buf.insert(0, fillChar);
			} else {
				buf.append(fillChar);
			}
		}
		return(buf.toString());
	}

	/**
	 * Removes all non USASCII chars and converts to uppercase. Swedish characters
	 * ÅÄÖ are replaced with A and O.
	 * @param clearupstring		The string to be cleared.
	 * @return	A string containing only ASCII
	 */
	public static String onlyUSASCII(String clearupstring) {
		if (clearupstring==null) return null;
		StringBuffer buf = new StringBuffer();
		String work = clearupstring.toUpperCase();
		char c;
		for (int i=0; i<work.length(); i++) {
			c = work.charAt(i);
			if (c=='Å' || c=='Ä') {
				buf.append("A");
				continue;
			}
			if (c=='Ö') {
				buf.append("O");
				continue;
			}
			if ((c>='0' && c<='9') || (c>='A' && c<='Z') || c==',' || c=='.' || c=='-') {
				buf.append(c);
			} else {
				// Blank out non-legal chars
				buf.append(" ");
			}
				
		}
		return(buf.toString());
	}

	/**
	 * Returns number of days from today
	 * @param	date	Date to check
	 * @return	The number of days to the given date
	 */
	public static int daysFromNow(java.util.Date date) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		long then = date.getTime();
		
		Calendar nowCal = Calendar.getInstance();
		nowCal.set(Calendar.HOUR_OF_DAY, 0);
		nowCal.set(Calendar.MINUTE, 0);
		nowCal.set(Calendar.SECOND, 0);
		nowCal.set(Calendar.MILLISECOND, 0);
		
		long now = nowCal.getTimeInMillis();
		long daysInMillis = then - now;
		long days = daysInMillis / 1000 / 60 / 60 / 24;
		
		return ((int)days);
	}
	
	/**
	 * Adds number of days on specified date
	 * @param	date	The date to add to
	 * @param	daysToAdd	The number of days to add.
	 * @return	The date after the days have been added.
	 */
	public static java.util.Date addDays(java.util.Date date, int daysToAdd) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		cal.add(Calendar.DATE, daysToAdd);
		
		return(cal.getTime());
		
	}
	
}
