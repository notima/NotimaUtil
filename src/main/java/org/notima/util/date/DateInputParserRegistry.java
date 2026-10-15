package org.notima.util.date;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.notima.util.date.se.SeDateInputParser;

/**
 * Registry of country specific {@link DateInputParser}s.
 *
 * <p>To support a new country, implement {@link DateInputParser} (preferably by extending
 * {@link IsoDateInputParser}, so the unambiguous ISO forms keep working) in a subpackage
 * named after the country, and register it below or at runtime with {@link #register}.
 *
 * <p>Countries without a registered parser get an {@link IsoDateInputParser}, which only
 * accepts unambiguous year-first dates.
 */
public class DateInputParserRegistry {

	private static final DateInputParser FALLBACK = new IsoDateInputParser();

	private static final Map<String, DateInputParser> parsers = new ConcurrentHashMap<>();

	static {
		register(new SeDateInputParser());
	}

	private DateInputParserRegistry() {}

	/** Registers {@code parser}; replaces any parser previously registered for the same country. */
	public static void register(DateInputParser parser) {
		parsers.put(parser.getCountryCode().toUpperCase(), parser);
	}

	/**
	 * Returns the parser for {@code countryCode} (case insensitive), or the ISO-only
	 * fallback parser if the country is unknown or {@code null}. Never {@code null}.
	 */
	public static DateInputParser forCountry(String countryCode) {
		if (countryCode == null) return FALLBACK;
		DateInputParser p = parsers.get(countryCode.trim().toUpperCase());
		return p != null ? p : FALLBACK;
	}

	/** Returns the parser for the country of {@code locale}. See {@link #forCountry(String)}. */
	public static DateInputParser forLocale(Locale locale) {
		return forCountry(locale != null ? locale.getCountry() : null);
	}

	/** Returns {@code true} if a parser is registered specifically for {@code countryCode}. */
	public static boolean isSupported(String countryCode) {
		return countryCode != null && parsers.containsKey(countryCode.trim().toUpperCase());
	}

}
