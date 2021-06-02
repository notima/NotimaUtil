package org.notima.test.util;

import java.text.ParseException;
import java.util.Calendar;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.notima.util.NotimaUtil;


public class TestBgUtil extends TestCase {

	@Test
	public void testGetDateString() {
		
		// Create specific date
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2010);
		cal.set(Calendar.MONTH, 1); // February
		cal.set(Calendar.DATE, 19);
		String result = NotimaUtil.getDateString(cal.getTime());
		Assert.assertEquals("100219", result);
		
	}

	@Test
	public void testRemoveBlanks() {
		
		String test = "SE123 231 201 92 ";
		String expected = "SE12323120192";
		
		Assert.assertEquals(expected, NotimaUtil.removeBlanks(test));
		
	}
	
	@Test
	public void testParseDateString() {
		
		try {
			
			java.util.Date date = NotimaUtil.parseDateString("091231");
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 2009);
			cal.set(Calendar.MONTH, 11); // December
			cal.set(Calendar.DATE, 31);
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.AM_PM, Calendar.AM);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Assert.assertEquals(date, cal.getTime());
			
			// Test "Genast"
			date = NotimaUtil.parseDateString("GENAST");
			Assert.assertNull(date);
			// Test "000000"
			date = NotimaUtil.parseDateString("000000");
			Assert.assertNull(date);
			
		} catch (ParseException pe) {
			Assert.fail(pe.getMessage());
		}
		
	}

	@Test
	public void testToDigitsOnly() {
		
		String result = NotimaUtil.toDigitsOnly("1920-1928-12389BH");
		Assert.assertEquals("1920192812389", result);
		
	}

	@Test
	public void testHasDigitsOnly() {
		
		boolean result = NotimaUtil.hasDigitsOnly("1234-1293");
		Assert.assertEquals(false, result);
		
		result = NotimaUtil.hasDigitsOnly("12343920");
		Assert.assertEquals(true, result);
		
	}

	@Test
	public void testTrimLeadingZeros() {
		
		String result = NotimaUtil.trimLeadingZeros("000123890");
		Assert.assertEquals("123890", result);
		
	}

	@Test
	public void testValidateBankgiro() {
	}

	@Test
	public void testFormatBg() {
		
		String result = NotimaUtil.formatBg("2703029");
		Assert.assertEquals("270-3029", result);
		
	}

	@Test
	public void testFormatPg() {
		
		String actual = NotimaUtil.formatPg("2093280");
		Assert.assertEquals("209328-0", actual);
		
	}

	@Test
	public void testGetAmountStr() {
		String result = NotimaUtil.getAmountStr(-100.23, 12, false);
		Assert.assertEquals("00000001002L", result);
	}

	@Test
	public void testParseAmountStr() {
	}

	@Test
	public void testGetLuhnDigit() {
		int result = NotimaUtil.getLuhnDigit("2876821");
		Assert.assertEquals(6, result);
	}

	@Test
	public void testToOCRNumber() {
	}
	
	@Test
	public void testToOCRNumberWithLengthCheck() {
		String result = NotimaUtil.toOCRNumberWithLengthCheck("750210001012079");
		Assert.assertEquals("75021000101207972", result);
	}
	

	@Test
	public void testIsValidOCRNumber() {
	}

	@Test
	public void testOnlyUSASCII() {
		
		String result = NotimaUtil.onlyUSASCII("UMEÅ");
		Assert.assertEquals("UMEA", result);
		result = NotimaUtil.onlyUSASCII("Gräddvägen");
		Assert.assertEquals("GRADDVAGEN", result);
		
	}
	
	@Test
	public void testDaysFromNow() {
		
		Calendar nowCal = Calendar.getInstance();
		nowCal.add(Calendar.DATE, 60);
		int days = NotimaUtil.daysFromNow(nowCal.getTime());
		Assert.assertEquals(60, days);
		
	}
	
	@Test
	public void testFillToLength() {
		
		String result = NotimaUtil.fillToLength("10", true, '0', 4);
		Assert.assertEquals("0010", result);
		
		result = NotimaUtil.fillToLength("10", false, '0', 4);
		Assert.assertEquals("1000", result);
		
		result = NotimaUtil.fillToLength(null, true, '0', 4);
		Assert.assertEquals("0000", result);
		
		result = NotimaUtil.fillToLength("100001", true, '0', 4);
		Assert.assertEquals("0001", result);
		
		result = NotimaUtil.fillToLength("100001", false, '0', 4);
		Assert.assertEquals("1000", result);
		
	}
	
	
}
