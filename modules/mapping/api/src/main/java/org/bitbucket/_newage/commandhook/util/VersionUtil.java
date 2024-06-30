package org.bitbucket._newage.commandhook.util;

import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VersionUtil {
    private static final Pattern BUKKIT_VERSION_PATTERN = Pattern.compile("(\\d+\\.\\d+(\\.\\d+)?)");
    private static final Pattern MINOR_VERSION_PATTERN = Pattern.compile("\\d+\\.(\\d+)(\\.\\d+)?");

    public static String getMinecraftVersion() {
        final Matcher bukkitVersionMatcher = BUKKIT_VERSION_PATTERN.matcher(Bukkit.getServer().getBukkitVersion());

        if (bukkitVersionMatcher.find()) {
            return bukkitVersionMatcher.group();
        }
        return null;
    }
    public static int getMinorVersion(String minecraftVersion) {
        final Matcher minorVersionMatcher = MINOR_VERSION_PATTERN.matcher(minecraftVersion);
        minorVersionMatcher.find();
        return Integer.parseInt(minorVersionMatcher.group(1));
    }
}
