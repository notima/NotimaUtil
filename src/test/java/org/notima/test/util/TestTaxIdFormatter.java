package org.notima.test.util;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.notima.util.InvalidTaxIdFormatException;
import org.notima.util.TaxIdFormatter;
import org.notima.util.TaxIdStructure;
import org.notima.util.UnknownTaxIdFormatException;

public class TestTaxIdFormatter {

	private static final LocalDate REF = LocalDate.of(2026, 10, 19);

	private static String print(String taxId, String dstFormat) throws Exception {
		return TaxIdFormatter.printTaxId(null, taxId, dstFormat, REF);
	}

	@Test
	public void determinesFormat() {
		assertEquals(TaxIdStructure.FMT_SE10, TaxIdFormatter.determineFormat("SE", "5569543738"));
		assertEquals(TaxIdStructure.FMT_SE11, TaxIdFormatter.determineFormat("SE", "556954-3738"));
		assertEquals(TaxIdStructure.FMT_SE11, TaxIdFormatter.determineFormat("SE", "120101+1234"));
		assertEquals(TaxIdStructure.FMT_SE12, TaxIdFormatter.determineFormat("SE", "198001011234"));
		assertEquals(TaxIdStructure.FMT_SE13, TaxIdFormatter.determineFormat("SE", "19800101-1234"));
		assertEquals(TaxIdStructure.FMT_SE14, TaxIdFormatter.determineFormat("SE", "SE556954373801"));
		assertEquals(TaxIdStructure.FMT_SE11, TaxIdFormatter.determineFormat(null, " 556954 - 3738 "));
		assertEquals(TaxIdStructure.FMT_SE14, TaxIdFormatter.determineFormat(null, "se 5569543738 01"));
		assertEquals(TaxIdStructure.FMT_UNKNOWN, TaxIdFormatter.determineFormat("NO", "5569543738"));
		assertEquals(TaxIdStructure.FMT_UNKNOWN, TaxIdFormatter.determineFormat(null, "55695-43738"));
	}

	@Test
	public void printsOrganisationNumber() throws Exception {
		for (String in : new String[] {"5569543738", "556954-3738", "165569543738", "16556954-3738", "SE556954373801"}) {
			assertEquals("5569543738", print(in, TaxIdStructure.FMT_SE10), in);
			assertEquals("556954-3738", print(in, TaxIdStructure.FMT_SE11), in);
			assertEquals("165569543738", print(in, TaxIdStructure.FMT_SE12), in);
			assertEquals("16556954-3738", print(in, TaxIdStructure.FMT_SE13), in);
			assertEquals("SE556954373801", print(in, TaxIdStructure.FMT_SE14), in);
		}
	}

	@Test
	public void printsPersonalNumber() throws Exception {
		for (String in : new String[] {"8001011234", "800101-1234", "198001011234", "19800101-1234"}) {
			assertEquals("8001011234", print(in, TaxIdStructure.FMT_SE10), in);
			assertEquals("800101-1234", print(in, TaxIdStructure.FMT_SE11), in);
			assertEquals("198001011234", print(in, TaxIdStructure.FMT_SE12), in);
			assertEquals("19800101-1234", print(in, TaxIdStructure.FMT_SE13), in);
		}
	}

	@Test
	public void completesCenturyFromReferenceDate() throws Exception {
		assertEquals("201001011234", print("100101-1234", TaxIdStructure.FMT_SE12));
		assertEquals("192701011234", print("270101-1234", TaxIdStructure.FMT_SE12));
		assertEquals("202601011234", print("260101-1234", TaxIdStructure.FMT_SE12));
	}

	@Test
	public void plusMeansHundredOrOlder() throws Exception {
		assertEquals("191201011234", print("120101+1234", TaxIdStructure.FMT_SE12));
		assertEquals("120101+1234", print("191201011234", TaxIdStructure.FMT_SE11));
		assertEquals("120101-1234", print("201201011234", TaxIdStructure.FMT_SE11));
	}

	@Test
	public void recognisesSamordningsnummer() throws Exception {
		TaxIdStructure t = TaxIdFormatter.parseTaxId(null, "800161-1234");	// day 1 + 60
		assertTrue(t.isPersonalNumberSE());
		assertEquals("198001611234", t.printTaxId(TaxIdStructure.FMT_SE12, REF));
	}

	@Test
	public void tellsPersonsFromOrganisations() throws Exception {
		assertTrue(TaxIdFormatter.parseTaxId(null, "800101-1234").isPersonalNumberSE());
		assertFalse(TaxIdFormatter.parseTaxId(null, "556954-3738").isPersonalNumberSE());
		assertFalse(TaxIdFormatter.parseTaxId(null, "165569543738").isPersonalNumberSE());
		assertFalse(TaxIdFormatter.parseTaxId(null, "801301-1234").isPersonalNumberSE());	// month 13
	}

	@Test
	public void rejectsInvalidInput() {
		assertThrows(UnknownTaxIdFormatException.class, () -> print("12345", TaxIdStructure.FMT_SE10));
		assertThrows(UnknownTaxIdFormatException.class, () -> print("5569543738", "XX"));
		assertThrows(InvalidTaxIdFormatException.class,
				() -> TaxIdFormatter.printTaxId(TaxIdStructure.FMT_SE12, "556954-3738", TaxIdStructure.FMT_SE10));
	}
}
