package org.notima.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
	
	/**
     * Inserts a new directory name before the file name in the provided file path.
     * 
     * @param filePath the original file path as a String
     * @param newDirName the name of the new directory to insert
     * @return the modified file path as a String
     */
    public static String insertDirectory(String filePath, String newDirName) {
        // Create a Path object from the file path
        Path originalPath = Paths.get(filePath);

        // Ensure the provided path includes a file name
        if (originalPath.getFileName() == null) {
            throw new IllegalArgumentException("The provided path does not include a file name.");
        }

        // Extract the parent directory and file name
        Path parentDir = originalPath.getParent();
        Path fileName = originalPath.getFileName();

        // Create the new path with the inserted directory
        Path newPath = parentDir != null 
                ? parentDir.resolve(newDirName).resolve(fileName)
                : Paths.get(newDirName, fileName.toString());

        return newPath.toString();
    }	
	
    /**
     * Moves the original file to the new location, inserting a directory before the file name.
     * 
     * @param filePath the original file path as a String
     * @param newDirName the name of the new directory to insert
     * @throws IOException if an I/O error occurs while moving the file
     */
    public static void moveFileToNewDirectory(String filePath, String newDirName) throws IOException {
        Path originalPath = Paths.get(filePath);
        Path newPath = Paths.get(insertDirectory(filePath, newDirName));

        // Ensure the target directory exists
        Files.createDirectories(newPath.getParent());

        // Move the file to the new location
        Files.move(originalPath, newPath, StandardCopyOption.REPLACE_EXISTING);

    }    
    
}
