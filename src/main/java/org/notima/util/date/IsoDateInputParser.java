package org.notima.util.date;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Accepts only unambiguous, year-first dates with a four-digit year:
 * {@code YYYY-MM-DD}, {@code YYYYMMDD}, and {@code YYYY-M-D} with any of the separators
 * {@code - . / } or blank.
 *
 * <p>Used for countries without a registered parser, and as the base class of country
 * specific parsers, which add the local conventions on top. The helpers for building a
 * validated date and expanding two-digit years are available to subclasses.
 */
public class IsoDateInputParser implements DateInputParser {

	private static final Pattern	isoDigits	= Pattern.compile("^(\\d{4})(\\d{2})(\\d{2})$");
	private static final Pattern	isoSep		= Pattern.compile("^(\\d{4})[-./ ](\\d{1,2})[-./ ](\\d{1,2})$");

	@Override
	public String getCountryCode() {
		return null;
	}

	@Override
	public LocalDate parse(String text, LocalDate referenceDate) {
		String s = clean(text);
		if (s.isEmpty()) return null;
		return parseIso(s);
	}

	/**
	 * Parses an already {@link #clean cleaned} string in one of the ISO forms.
	 *
	 * @return	The date, or {@code null} if it's not an ISO date.
	 */
	protected LocalDate parseIso(String s) {
		Matcher m = isoDigits.matcher(s);
		if (!m.matches()) m = isoSep.matcher(s);
		if (!m.matches()) return null;
		return toDate(m.group(1), m.group(2), m.group(3));
	}

	/**
	 * Trims, lower cases and collapses inner white space to single blanks.
	 * Returns an empty string for {@code null}.
	 */
	protected static String clean(String text) {
		if (text == null) return "";
		return text.trim().toLowerCase().replaceAll("\\s+", " ");
	}

	/**
	 * Creates a date from numeric strings.
	 *
	 * @return	The date, or {@code null} if it doesn't exist (e.g. February 30th).
	 */
	protected static LocalDate toDate(String year, String month, String day) {
		return toDate(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
	}

	/**
	 * Creates a date.
	 *
	 * @return	The date, or {@code null} if it doesn't exist (e.g. February 30th).
	 */
	protected static LocalDate toDate(int year, int month, int day) {
		try {
			return LocalDate.of(year, month, day);
		} catch (DateTimeException e) {
			return null;
		}
	}

	/**
	 * Expands a two-digit year to four digits, choosing the century that puts the year
	 * within 80 years before and 20 years after the reference year (the same sliding
	 * window as {@link java.text.SimpleDateFormat}). With 2026 as reference, 46 becomes
	 * 2046 and 47 becomes 1947. Four-digit years are returned unchanged.
	 */
	protected static int expandYear(String year, LocalDate referenceDate) {
		int y = Integer.parseInt(year);
		if (year.length() > 2) return y;
		int refYear = referenceDate.getYear();
		int candidate = refYear - Math.floorMod(refYear, 100) + y;
		if (candidate > refYear + 20) candidate -= 100;
		else if (candidate <= refYear - 80) candidate += 100;
		return candidate;
	}

}
