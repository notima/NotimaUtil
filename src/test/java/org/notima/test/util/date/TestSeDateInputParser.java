package org.notima.test.util.date;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.Locale;

import org.junit.Test;
import org.notima.util.date.DateInputParser;
import org.notima.util.date.DateInputParserRegistry;
import org.notima.util.date.IsoDateInputParser;
import org.notima.util.date.se.SeDateInputParser;

public class TestSeDateInputParser {

	private static final LocalDate REF = LocalDate.of(2026, 10, 15);

	private final DateInputParser parser = DateInputParserRegistry.forCountry("se");

	private void assertParses(String expected, String input) {
		assertEquals("Input: " + input, LocalDate.parse(expected), parser.parse(input, REF));
	}

	private void assertRejected(String input) {
		assertNull("Input: " + input, parser.parse(input, REF));
	}

	@Test
	public void testRegistry() {
		assertTrue(parser instanceof SeDateInputParser);
		assertTrue(DateInputParserRegistry.forLocale(new Locale("sv", "SE")) instanceof SeDateInputParser);
		assertEquals(IsoDateInputParser.class, DateInputParserRegistry.forCountry("XX").getClass());
		assertEquals(IsoDateInputParser.class, DateInputParserRegistry.forCountry(null).getClass());
	}

	@Test
	public void testIso() {
		assertParses("2026-10-15", "2026-10-15");
		assertParses("2026-10-15", " 20261015 ");
		assertParses("2026-10-15", "2026.10.15");
		assertParses("2026-10-15", "2026 10 15");
		assertParses("2026-10-15", "2026/10/15");
		assertParses("2026-03-05", "2026-3-5");
	}

	@Test
	public void testShortYear() {
		assertParses("2025-12-31", "251231");
		assertParses("2026-10-15", "26-10-15");
		assertParses("2026-10-15", "26-1015");
		assertParses("2026-10-15", "2610-15");
		assertParses("2026-10-15", "2026-1015");
		assertParses("2026-10-15", "202610-15");
		assertParses("1999-12-31", "991231");
		assertParses("2046-01-01", "460101");
		assertParses("1947-01-01", "470101");
	}

	@Test
	public void testCurrentYearAndMonth() {
		assertParses("2026-10-15", "1015");
		assertParses("2026-03-15", "315");
		assertParses("2026-10-15", "10-15");
		assertParses("2026-03-05", "3-5");
		assertParses("2026-10-15", "15");
		assertParses("2026-10-05", "5");
		assertParses("2026-10-31", "31");
	}

	@Test
	public void testDayFirst() {
		assertParses("2026-10-15", "15/10");
		assertParses("2026-10-15", "15/10-26");
		assertParses("2026-10-15", "15/10 2026");
		assertParses("2026-10-15", "15/10/26");
		assertParses("2026-03-05", "5/3");
		assertParses("2026-10-15", "15.10.2026");
	}

	@Test
	public void testMonthNames() {
		assertParses("2026-10-15", "15 okt");
		assertParses("2026-10-15", "15 Oktober 2026");
		assertParses("2026-10-15", "15:e okt -26");
		assertParses("2026-05-01", "1 maj");
		assertParses("2026-03-01", "1 mar");
		assertParses("2026-09-01", "1 sept.");
		assertRejected("1 ma");
		assertRejected("1 foo");
	}

	@Test
	public void testInvalid() {
		assertNull(parser.parse(null, REF));
		assertRejected("");
		assertRejected("   ");
		assertRejected("0230");
		assertRejected("32");
		assertRejected("0");
		assertRejected("12345");
		assertRejected("1234567");
		assertRejected("2026-13-01");
		assertRejected("2026-10");
		assertRejected("abc");
		assertRejected("15 10");		// would be M-D: month 15
	}

	@Test
	public void testLeapYear() {
		assertParses("2024-02-29", "240229");
		assertRejected("250229");
	}

	@Test
	public void testIsoFallbackIsStrict() {
		DateInputParser iso = DateInputParserRegistry.forCountry(null);
		assertEquals(LocalDate.of(2026, 10, 15), iso.parse("20261015", REF));
		assertNull(iso.parse("1015", REF));
		assertNull(iso.parse("15/10", REF));
	}

}
