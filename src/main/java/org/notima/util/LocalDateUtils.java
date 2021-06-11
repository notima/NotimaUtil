package org.notima.util;

import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


/**
 * Converts between Date and LocalDate
 * 
 * @author Lokesh Gupta
 * @author Daniel Tamm
 *
 */
public class LocalDateUtils {
 
    public static Date asDate(LocalDate localDate) {
    	if (localDate==null) return null;
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
 
    public static Date asDate(LocalDateTime localDateTime) {
    	if (localDateTime==null) return null;
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
 
    public static LocalDate asLocalDate(Date date) {
    	if (date==null) return null;
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }
 
    public static LocalDateTime asLocalDateTime(Date date) {
    	if (date==null) return null;
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    
    /**
     * Prints a date range string using dash '_' as default range indicator. If start and end date are the same, only
     * one date is printed.
     * 
     * @param startDate			Start date
     * @param endDate			End date
     * @param dtf				DateFormat. If none is selected (null), default locale (short) is used.
     * @param rangeSeparator	Separates the start date from the end date. If null, _ is used.
     * 
     * @return
     */
    public static String getDateRangeStr(Date startDate, Date endDate, DateFormat dtf, String rangeSeparator) {
    	
    	if (dtf==null) {
    		dtf = DateFormat.getDateInstance(DateFormat.SHORT);
    	}
    	if (rangeSeparator==null) {
    		rangeSeparator = "_";
    	}
    	StringBuffer buf = new StringBuffer();
    	if (startDate.equals(endDate)) {
    		buf.append(dtf.format(startDate));
    	} else {
    		buf.append(dtf.format(startDate) + rangeSeparator + dtf.format(endDate));
    	}

    	return buf.toString();
    }
    
}