package org.notima.test.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.notima.util.ConfigFileResolver;

class ConfigFileResolverTest {

    @TempDir
    Path tempDir;

    private void info(String msg) {
        System.out.println("[TEST] " + msg);
    }

    @Test
    void returnsCandidateWhenCandidateIsExistingRegularFile() throws IOException {
        Path candidate = Files.createTempFile(tempDir, "cfg-", ".properties");

        info("Test: existing regular file");
        info("Candidate path = " + candidate);

        Path resolved = ConfigFileResolver.resolveFileOrOsConfigFallback(
                candidate.toString(),
                "MyApp",
                "fallback.properties"
        );

        info("Resolved path  = " + resolved);

        assertEquals(candidate.toAbsolutePath().normalize(),
                     resolved.toAbsolutePath().normalize());
    }

    @Test
    void fallsBackWhenCandidateIsDirectory() throws IOException {
        Path candidateDir = Files.createDirectory(tempDir.resolve("not-a-file"));

        String appName = "MyApp";
        String fallbackFileName = "fallback.properties";

        info("Test: candidate is a directory");
        info("Candidate path = " + candidateDir);

        Path resolved = ConfigFileResolver.resolveFileOrOsConfigFallback(
                candidateDir.toString(),
                appName,
                fallbackFileName
        );

        Path expectedFallback = ConfigFileResolver.osConfigFile(appName, fallbackFileName);

        info("Resolved path  = " + resolved);
        info("Fallback path = " + expectedFallback);

        assertEquals(expectedFallback.toAbsolutePath().normalize(),
                     resolved.toAbsolutePath().normalize());

        Path expectedBaseDir = ConfigFileResolver.osConfigDir(appName).toAbsolutePath().normalize();
        info("Expected base config dir = " + expectedBaseDir);

        assertTrue(resolved.toAbsolutePath().normalize().startsWith(expectedBaseDir));
    }

    @Test
    void fallsBackWhenCandidatePathIsInvalidOrNotCreatable() {
        String invalid = "bad\0path";

        String appName = "MyApp";
        String fallbackFileName = "fallback.properties";

        info("Test: invalid candidate path");
        info("Candidate string = " + invalid.replace("\0", "\\0"));

        Path resolved = ConfigFileResolver.resolveFileOrOsConfigFallback(
                invalid,
                appName,
                fallbackFileName
        );

        Path expectedFallback = ConfigFileResolver.osConfigFile(appName, fallbackFileName);

        info("Resolved path  = " + resolved);
        info("Fallback path = " + expectedFallback);

        assertEquals(expectedFallback.toAbsolutePath().normalize(),
                     resolved.toAbsolutePath().normalize());
    }

    @Test
    void osConfigDirMatchesExpectedConventionsForCurrentOs() {
        String appName = "My App:Name?*";

        info("Test: OS-specific config directory");
        info("OS name      = " + System.getProperty("os.name"));
        info("User home   = " + System.getProperty("user.home"));
        info("APPDATA     = " + System.getenv("APPDATA"));
        info("LOCALAPPDATA= " + System.getenv("LOCALAPPDATA"));
        info("XDG_CONFIG_HOME = " + System.getenv("XDG_CONFIG_HOME"));

        Path dir = ConfigFileResolver.osConfigDir(appName).toAbsolutePath().normalize();

        info("Resolved config dir = " + dir);

        String os = System.getProperty("os.name", "").toLowerCase();
        boolean isWindows = os.contains("win");
        boolean isMac = os.contains("mac");

        if (isWindows) {
            info("Detected OS: Windows");
            String appData = firstNonBlank(System.getenv("APPDATA"), System.getenv("LOCALAPPDATA"));
            if (appData != null) {
                info("Expected base: " + appData);
                assertTrue(dir.startsWith(Paths.get(appData).toAbsolutePath().normalize()));
            } else {
                Path expected = Paths.get(System.getProperty("user.home"), "AppData", "Roaming")
                                     .toAbsolutePath().normalize();
                info("Expected base (fallback): " + expected);
                assertTrue(dir.startsWith(expected));
            }
        } else if (isMac) {
            info("Detected OS: macOS");
            Path expected = Paths.get(System.getProperty("user.home"),
                                      "Library", "Application Support")
                                 .toAbsolutePath().normalize();
            info("Expected base: " + expected);
            assertTrue(dir.startsWith(expected));
        } else {
            info("Detected OS: Linux / Unix");
            String xdg = System.getenv("XDG_CONFIG_HOME");
            if (xdg != null && !xdg.trim().isEmpty()) {
                info("Expected base: " + xdg);
                assertTrue(dir.startsWith(Paths.get(xdg.trim()).toAbsolutePath().normalize()));
            } else {
                Path expected = Paths.get(System.getProperty("user.home"), ".config")
                                     .toAbsolutePath().normalize();
                info("Expected base (fallback): " + expected);
                assertTrue(dir.startsWith(expected));
            }
        }

        String dirStr = dir.toString();
        info("Final sanitized path = " + dirStr);

        assertFalse(dirStr.matches(".*[<>:\"|?*].*"),
                "Sanitized app name must not contain Windows-illegal characters");
    }

    private static String firstNonBlank(String... values) {
        for (String v : values) {
            if (v != null && !v.trim().isEmpty()) return v.trim();
        }
        return null;
    }
}
