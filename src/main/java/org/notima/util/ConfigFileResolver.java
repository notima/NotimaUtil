package org.notima.util;

import java.io.IOException;
import java.nio.file.*;

/**
 * Utility class for resolving a usable configuration file path for an application.
 *
 * <p>The resolver accepts a user-supplied file path and determines whether it
 * represents a valid, regular file in the file system. If the path already
 * points to an existing file, it is used directly. If the file does not exist
 * but can be created (including creation of parent directories), the path is
 * also accepted.</p>
 *
 * <p>If the supplied path is invalid, points to something other than a regular
 * file (such as a directory or device), or cannot be created due to filesystem
 * or permission constraints, the resolver falls back to an operating-system
 * specific application configuration directory.</p>
 *
 * <p>The fallback location is derived solely from the application name and
 * follows common platform conventions:</p>
 * <ul>
 *   <li><b>Windows:</b> {@code %APPDATA%/&lt;appName&gt;/}</li>
 *   <li><b>macOS:</b> {@code ~/Library/Application Support/&lt;appName&gt;/}</li>
 *   <li><b>Linux / Unix:</b> {@code $XDG_CONFIG_HOME/&lt;appName&gt;/} or
 *       {@code ~/.config/&lt;appName&gt;/}</li>
 * </ul>
 *
 * <p>This class is intentionally defensive and avoids throwing exceptions for
 * common configuration errors. Its goal is to always return the best possible
 * file path that the application can safely use for reading or writing
 * configuration data.</p>
 *
 * <p>The class is stateless, thread-safe, and compatible with Java&nbsp;11.</p>
 */
public final class ConfigFileResolver {

    private ConfigFileResolver() {}

    public static Path resolveFileOrOsConfigFallback(String candidatePath,
                                                     String appName,
                                                     String fallbackFileName) {

        Path fallback = osConfigFile(appName, fallbackFileName);

        Path candidate;
        try {
            if (candidatePath == null || candidatePath.trim().isEmpty()) {
                return ensureCreatableOrFallback(fallback, fallback);
            }
            candidate = Paths.get(candidatePath.trim());
        } catch (InvalidPathException ex) {
            return ensureCreatableOrFallback(fallback, fallback);
        }

        if (Files.isRegularFile(candidate)) {
            return candidate;
        }

        if (Files.exists(candidate)) {
            // Exists but not a regular file (dir, device, etc.)
            return ensureCreatableOrFallback(fallback, fallback);
        }

        if (canCreateFile(candidate)) {
            return candidate;
        }

        return ensureCreatableOrFallback(fallback, fallback);
    }

    /**
     * OS-specific config directory + appName + fallbackFileName.
     */
    public static Path osConfigFile(String appName, String fallbackFileName) {
        String safeApp = sanitizeAppName(appName);
        String file = (fallbackFileName == null || fallbackFileName.trim().isEmpty())
                ? "app.properties"
                : fallbackFileName.trim();

        Path base = osConfigDir(safeApp);
        return base.resolve(file);
    }

    /**
     * OS-specific "settings" dir for the app (directory only).
     */
    public static Path osConfigDir(String appName) {
        String safeApp = sanitizeAppName(appName);

        String os = System.getProperty("os.name", "").toLowerCase();
        boolean isWindows = os.contains("win");
        boolean isMac = os.contains("mac");

        if (isWindows) {
            // Prefer Roaming AppData
            String appData = firstNonBlank(System.getenv("APPDATA"),
                                           System.getenv("LOCALAPPDATA"));
            if (appData != null) {
                return Paths.get(appData).resolve(safeApp);
            }
            // Last resort
            return userHome().resolve("AppData").resolve("Roaming").resolve(safeApp);
        }

        if (isMac) {
            return userHome()
                    .resolve("Library")
                    .resolve("Application Support")
                    .resolve(safeApp);
        }

        // Linux / other Unix: XDG Base Directory Spec
        String xdg = System.getenv("XDG_CONFIG_HOME");
        if (xdg != null && !xdg.trim().isEmpty()) {
            return Paths.get(xdg.trim()).resolve(safeApp);
        }

        return userHome().resolve(".config").resolve(safeApp);
    }

    private static Path userHome() {
        String home = System.getProperty("user.home");
        if (home == null || home.trim().isEmpty()) {
            return Paths.get(".");
        }
        return Paths.get(home);
    }

    private static String sanitizeAppName(String appName) {
        String s = (appName == null) ? "" : appName.trim();
        if (s.isEmpty()) s = "app";
        // Keep it filesystem-friendly across OSes
        s = s.replaceAll("[\\\\/:*?\"<>|]", "_"); // Windows-illegal chars
        s = s.replaceAll("\\s+", " ").trim();
        return s;
    }

    private static String firstNonBlank(String... values) {
        for (String v : values) {
            if (v != null && !v.trim().isEmpty()) return v.trim();
        }
        return null;
    }

    private static boolean canCreateFile(Path file) {
        try {
            Path parent = file.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            if (Files.exists(file) && !Files.isRegularFile(file)) {
                return false;
            }

            if (!Files.exists(file)) {
                Files.createFile(file);
                // Try to clean up, but if deletion fails, we still proved creatable
                try { Files.deleteIfExists(file); } catch (IOException ignore) {}
            }

            return true;
        } catch (IOException | SecurityException | InvalidPathException ex) {
            return false;
        }
    }

    private static Path ensureCreatableOrFallback(Path file, Path fallback) {
        if (Files.isRegularFile(file)) return file;
        if (Files.exists(file) && !Files.isRegularFile(file)) return fallback;
        if (canCreateFile(file)) return file;
        return fallback;
    }
}
