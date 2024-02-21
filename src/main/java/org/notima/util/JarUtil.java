package org.notima.util;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarUtil {

	private ClassLoader classLoader;
	
	/**
	 * Creates a Jar util using the supplied classloader.
	 * 
	 * @param cl
	 */
	public JarUtil(ClassLoader cl) {
		classLoader = cl;
	}
	
	public void copyResourcesToDirectory(String sourceDir, String destinationDir) {
        try {
            // Get the URL of the resource directory
            URL resourceDirURL = classLoader.getResource(sourceDir);
            if (resourceDirURL == null) {
                throw new RuntimeException("Resource directory not found: " + sourceDir);
            }

            // Convert URL to a URI
            URI resourceDirURI = resourceDirURL.toURI();

            // Check if the resource is within a JAR file
            if ("jar".equals(resourceDirURI.getScheme())) {
                // If it's within a JAR, use JarURLConnection to access its contents
                JarURLConnection connection = (JarURLConnection) resourceDirURL.openConnection();
                try (JarFile jarFile = connection.getJarFile()) {
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        if (!entry.isDirectory() && entry.getName().startsWith(sourceDir)) {
                            Path destination = Paths.get(destinationDir, entry.getName().substring(sourceDir.length()));
                            Files.copy(jarFile.getInputStream(entry), destination, StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                }
            } else {
                // If it's not within a JAR, simply copy the files
                Files.walk(Paths.get(resourceDirURI))
                        .forEach(source -> {
                            try {
                                Path destination = Paths.get(destinationDir, Paths.get(resourceDirURI).relativize(source).toString());
                                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
	
}
