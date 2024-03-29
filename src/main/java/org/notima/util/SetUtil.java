package org.notima.util;

import java.util.Iterator;
import java.util.Set;

public class SetUtil {

	/**
	 * Converts a set to a comma separated string.
	 * 
	 * @param set		The set to convert
	 * @return	A string separated by comma.
	 */
	public static String setToCommaSeparatedString(Set<?> set) {

		if (set==null || set.size()==0) 
			return "";
		
		StringBuffer buf = new StringBuffer();
		Object s;
		for (Iterator<?> i = set.iterator() ; i.hasNext();) {
			s = i.next();
			buf.append(s.toString());
			if (i.hasNext()) {
				buf.append(", ");
			}
		}
		return buf.toString();
		
	}
	
}
