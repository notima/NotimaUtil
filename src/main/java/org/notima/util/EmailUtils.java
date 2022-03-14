package org.notima.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtils {

	public static Pattern validEmailPattern = Pattern.compile("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$");
	
	public static boolean isValidEmail(String email) {

		if (email==null) return false;
		
		Matcher m = validEmailPattern.matcher(email);
		return m.matches();
		
	}
	
}
