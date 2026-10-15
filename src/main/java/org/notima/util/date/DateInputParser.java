package org.notima.util.date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Interprets dates the way people of a specific country type them by hand.
 *
 * <p>How a date is written differs between countries (is {@code 3/5} the 3rd of May or
 * March 5th?), so each country has its own implementation, registered in
 * {@link DateInputParserRegistry}. Incomplete input is completed from a reference date
 * (normally today), e.g. a day number alone means that day in the reference month.
 *
 * <pre>
 *   DateInputParser parser = DateInputParserRegistry.forCountry("SE");
 *   LocalDate d   = parser.parse("251231");       // 2025-12-31
 *   String    iso = parser.normalize("1015");     // "2026-10-15" (if run in 2026)
 * </pre>
 */
public interface DateInputParser {

	/** ISO 3166-1 alpha-2 country code this parser applies to, e.g. {@code "SE"}. */
	String getCountryCode();

	/**
	 * Parses {@code text} as a date.
	 *
	 * @param text			The date as typed by the user.
	 * @param referenceDate	Supplies the year/month that is left out of incomplete input,
	 * 						and anchors the century of two-digit years.
	 * @return				The date, or {@code null} if {@code text} is empty or can't be
	 * 						interpreted as a valid date.
	 */
	LocalDate parse(String text, LocalDate referenceDate);

	/** Parses {@code text} relative to today. See {@link #parse(String, LocalDate)}. */
	default LocalDate parse(String text) {
		return parse(text, LocalDate.now());
	}

	/**
	 * Parses {@code text} relative to today and prints it as an ISO date (YYYY-MM-DD).
	 *
	 * @return	The ISO date, or {@code null} if {@code text} can't be interpreted.
	 */
	default String normalize(String text) {
		LocalDate d = parse(text);
		return d != null ? d.format(DateTimeFormatter.ISO_LOCAL_DATE) : null;
	}

}
