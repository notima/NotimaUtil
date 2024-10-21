package org.notima.util.comm;

import java.io.File;
import java.io.IOException;

public class ScpSystemTransfer {
    public static void scpFileToRemote(String localFilePath, String remoteDestination) throws IOException, InterruptedException {
        // Validate the local file
        File localFile = new File(localFilePath);
        if (!localFile.exists()) {
            throw new IllegalArgumentException("Local file does not exist: " + localFilePath);
        }

        // Construct the command: scp localFilePath user@host:/path/on/remote
        ProcessBuilder processBuilder = new ProcessBuilder("scp", localFilePath, remoteDestination);

        // Inherit the system's environment (to use .ssh/config settings)
        processBuilder.inheritIO();

        // Start the process
        Process process = processBuilder.start();

        // Wait for the process to complete and get the exit status
        int exitCode = process.waitFor();

        // Check if the command was successful
        if (exitCode != 0) {
            throw new IOException("SCP command failed with exit code: " + exitCode);
        }
    }
}