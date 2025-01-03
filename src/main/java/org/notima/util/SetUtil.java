package org.notima.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
	
	/**
	 * Converts a set to a list of strings.
	 * 
	 * @param set		The set to convert
	 * @return			A list of strings separated by comma.
	 */
	public static List<String> setToStringList(Set<?> set) {

		List<String> result = new ArrayList();
		
		if (set==null || set.size()==0) 
			return result;
		
		Object s;
		for (Iterator<?> i = set.iterator() ; i.hasNext();) {
			s = i.next();
			result.add(s.toString());
		}
		return result;
		
	}
	
	
}
