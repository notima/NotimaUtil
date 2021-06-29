package org.notima.util;

import java.util.List;

public class ListUtil {

	/**
	 * Converts a list to a comma separated string.
	 * 
	 * @param list
	 * @return	A string separated by comma.
	 */
	public static String ListToCommaSeparatedString(List<?> list) {

		if (list==null || list.size()==0) 
			return "";
		
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<list.size(); i++) {
			if (list.get(i)!=null) {
				buf.append(list.get(i).toString());
				if ((i+1)<list.size()) {
					buf.append(", ");
				}
			}
		}
		return buf.toString();
		
	}
	
}
