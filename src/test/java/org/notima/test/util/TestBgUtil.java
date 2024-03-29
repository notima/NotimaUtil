package org.notima.test.util;

import java.util.Calendar;

import org.junit.Test;
import org.notima.util.NotimaUtil;

import junit.framework.TestCase;


public class TestBgUtil extends TestCase {


	@Test
	public void testRemoveBlanks() {
		
		String test = "SE123 231 201 92 ";
		String expected = "SE12323120192";
		
		assertEquals(expected, NotimaUtil.removeBlanks(test));
		
	}

	@Test
	public void testToDigitsOnly() {
		
		String result = NotimaUtil.toDigitsOnly("1920-1928-12389BH");
		assertEquals("1920192812389", result);
		
	}

	@Test
	public void testHasDigitsOnly() {
		
		boolean result = NotimaUtil.hasDigitsOnly("1234-1293");
		assertEquals(false, result);
		
		result = NotimaUtil.hasDigitsOnly("12343920");
		assertEquals(true, result);
		
	}

	@Test
	public void testTrimLeadingZeros() {
		
		String result = NotimaUtil.trimLeadingZeros("000123890");
		assertEquals("123890", result);
		
	}

	@Test
	public void testValidateBankgiro() {
	}

	@Test
	public void testGetAmountStr() {
		String result = NotimaUtil.getAmountStr(-100.23, 12, false);
		assertEquals("00000001002L", result);
	}

	@Test
	public void testParseAmountStr() {
	}

	@Test
	public void testGetLuhnDigit() {
		int result = NotimaUtil.getLuhnDigit("2876821");
		assertEquals(6, result);
	}

	@Test
	public void testToOCRNumber() {
	}
	
	@Test
	public void testToOCRNumberWithLengthCheck() {
		String result = NotimaUtil.toOCRNumberWithLengthCheck("750210001012079");
		assertEquals("75021000101207972", result);
	}
	

	@Test
	public void testIsValidOCRNumber() {
	}

	@Test
	public void testOnlyUSASCII() {
		
		String result = NotimaUtil.onlyUSASCII("UMEÅ");
		assertEquals("UMEA", result);
		result = NotimaUtil.onlyUSASCII("Gräddvägen");
		assertEquals("GRADDVAGEN", result);
		
	}
	
	@Test
	public void testDaysFromNow() {
		
		Calendar nowCal = Calendar.getInstance();
		nowCal.add(Calendar.DATE, 60);
		int days = NotimaUtil.daysFromNow(nowCal.getTime());
		assertEquals(60, days);
		
	}
	
	@Test
	public void testFillToLength() {
		
		String result = NotimaUtil.fillToLength("10", true, '0', 4);
		assertEquals("0010", result);
		
		result = NotimaUtil.fillToLength("10", false, '0', 4);
		assertEquals("1000", result);
		
		result = NotimaUtil.fillToLength(null, true, '0', 4);
		assertEquals("0000", result);
		
		result = NotimaUtil.fillToLength("100001", true, '0', 4);
		assertEquals("0001", result);
		
		result = NotimaUtil.fillToLength("100001", false, '0', 4);
		assertEquals("1000", result);
		
	}
	
	
}
