package org.notima.util;

import java.io.File;

public class FileUtils {


	/**
	 * Remove unwanted characters from filename
	 * 
	 * @param suggestedFileName		Suggested file name.
	 * @return		A cleaned filename
	 */
	public static String fileNameSafe(String suggestedFileName) {
	
		String tmp = suggestedFileName.replace(" ", "_");		// Replace spaces
		tmp = tmp.replace(File.separator, "-");				// Replace any separator chars
		tmp = tmp.replace("&", "");							// Replace any ampersands.
		tmp = tmp.replace(",", "");
		tmp = tmp.replace("\"", "");
		tmp = tmp.replace("'", "");
		
		return tmp;
		
	}
	
}
