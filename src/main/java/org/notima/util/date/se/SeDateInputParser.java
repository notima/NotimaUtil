package org.notima.util.date.se;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.notima.util.date.IsoDateInputParser;

/**
 * Interprets dates the way they are commonly typed in Sweden. The examples assume the
 * reference date (today) is 2026-10-15.
 *
 * <pre>
 *   2026-10-15, 20261015, 2026.10.15, 2026 10 15   → 2026-10-15   (ISO)
 *   261015, 26-10-15, 26-1015, 2610-15             → 2026-10-15   (YYMMDD in any grouping)
 *   2026-1015, 202610-15                           → 2026-10-15
 *   1015, 10-15                                    → 2026-10-15   (MMDD, reference year)
 *   315, 3-5                                       → 2026-03-15, 2026-03-05
 *   15, 5                                          → 2026-10-15, 2026-10-05   (day in reference month)
 *   15/10, 15/10-26, 15/10 2026, 15/10/26          → 2026-10-15   (informal day/month)
 *   15.10.2026                                     → 2026-10-15   (day first, four-digit year last)
 *   15 okt, 15 oktober 2026, 15:e okt -26          → 2026-10-15   (Swedish month names)
 * </pre>
 *
 * Two-digit years are expanded to the century within 80 years before and 20 years after
 * the reference year. Input that matches a form but isn't a real date (e.g. {@code 0230})
 * gives {@code null}.
 */
public class SeDateInputParser extends IsoDateInputParser {

	private static final String[] MONTHS = {
			"januari", "februari", "mars", "april", "maj", "juni",
			"juli", "augusti", "september", "oktober", "november", "december" };

	private static final Pattern	digitsOnly		= Pattern.compile("^\\d{1,8}$");
	private static final Pattern	shortYearFirst	= Pattern.compile("^(\\d{2})[-. ](\\d{1,2})[-. ](\\d{1,2})$");
	private static final Pattern	twoGroups		= Pattern.compile("^(\\d+)[-. ](\\d+)$");
	private static final Pattern	dayFirstFullYear = Pattern.compile("^(\\d{1,2})[-.](\\d{1,2})[-.](\\d{4})$");
	private static final Pattern	dayMonthSlash	= Pattern.compile("^(\\d{1,2})/(\\d{1,2})(?:[-/ ] ?(\\d{4}|\\d{2}))?$");
	private static final Pattern	monthName		= Pattern.compile("^(\\d{1,2})(?::[ae])?\\.? ?(\\p{L}+)\\.?(?:[ ,-]*(\\d{4}|\\d{2}))?$");

	@Override
	public String getCountryCode() {
		return "SE";
	}

	@Override
	public LocalDate parse(String text, LocalDate referenceDate) {
		String s = clean(text);
		if (s.isEmpty()) return null;
		if (referenceDate == null) referenceDate = LocalDate.now();

		LocalDate d = parseIso(s);
		if (d != null) return d;

		if (digitsOnly.matcher(s).matches()) {
			return parseDigits(s, referenceDate);
		}

		Matcher m = shortYearFirst.matcher(s);
		if (m.matches()) {
			return toDate(expandYear(m.group(1), referenceDate), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)));
		}

		m = twoGroups.matcher(s);
		if (m.matches()) {
			String a = m.group(1);
			String b = m.group(2);
			if (a.length() <= 2 && b.length() <= 2) {
				// M-D
				return toDate(referenceDate.getYear(), Integer.parseInt(a), Integer.parseInt(b));
			}
			int total = a.length() + b.length();
			if (total == 6 || total == 8) {
				// YY-MMDD, YYMM-DD, YYYY-MMDD, YYYYMM-DD
				return parseDigits(a + b, referenceDate);
			}
			return null;
		}

		m = dayFirstFullYear.matcher(s);
		if (m.matches()) {
			return toDate(m.group(3), m.group(2), m.group(1));
		}

		m = dayMonthSlash.matcher(s);
		if (m.matches()) {
			return toDate(yearOrReference(m.group(3), referenceDate), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(1)));
		}

		m = monthName.matcher(s);
		if (m.matches()) {
			int month = monthFromName(m.group(2));
			if (month < 1) return null;
			return toDate(yearOrReference(m.group(3), referenceDate), month, Integer.parseInt(m.group(1)));
		}

		return null;
	}

	/**
	 * Interprets a string of digits by its length: D, DD, MDD, MMDD, YYMMDD or YYYYMMDD.
	 */
	private static LocalDate parseDigits(String s, LocalDate referenceDate) {
		int len = s.length();
		switch (len) {
			case 1:
			case 2:
				return toDate(referenceDate.getYear(), referenceDate.getMonthValue(), Integer.parseInt(s));
			case 3:
			case 4:
				return toDate(referenceDate.getYear(), Integer.parseInt(s.substring(0, len - 2)), Integer.parseInt(s.substring(len - 2)));
			case 6:
				return toDate(expandYear(s.substring(0, 2), referenceDate), Integer.parseInt(s.substring(2, 4)), Integer.parseInt(s.substring(4)));
			case 8:
				return toDate(s.substring(0, 4), s.substring(4, 6), s.substring(6));
			default:
				return null;
		}
	}

	private static int yearOrReference(String year, LocalDate referenceDate) {
		return year != null ? expandYear(year, referenceDate) : referenceDate.getYear();
	}

	/**
	 * Returns the month number (1-12) of a Swedish month name or an abbreviation of at
	 * least three letters (e.g. "okt", "sept"), or -1 if not recognized.
	 */
	private static int monthFromName(String name) {
		if (name.length() < 3) return -1;
		for (int i = 0; i < MONTHS.length; i++) {
			if (MONTHS[i].startsWith(name)) return i + 1;
		}
		return -1;
	}

}
