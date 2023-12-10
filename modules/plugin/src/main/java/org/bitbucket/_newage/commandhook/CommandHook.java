package org.bitbucket._newage.commandhook;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bitbucket._newage.commandhook.mapping.NmsMappingSelector;
import org.bitbucket._newage.commandhook.mapping.api.IMapping;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Plugin entry point.
 */
public class CommandHook extends JavaPlugin {
    private static final Pattern NMS_PATTERN = Pattern.compile("(v\\d+_\\d+_\\w+)");
    private static final Pattern BUKKIT_VERSION_PATTERN = Pattern.compile("(\\d+\\.\\d+(\\.\\d+)?)");
    private final Logger logger = LoggerFactory.getLogger(CommandHook.class);

    public void onEnable() {
        IMapping mapping = getMappingFromBukkitVersion();
        if (mapping == null) {
            logger.warn("Unable to obtain Minecraft version, falling back to NMS parsing");
            mapping = getMappingFromNms();
        }

        if (mapping == null) {
            logger.error("Unable to obtain NMS package, plugin will not work.");
            return;
        }

        getServer().getPluginManager().registerEvents(new CommandBlockListener(mapping), this);
    }

    private IMapping getMappingFromBukkitVersion() {
        final Matcher bukkitVersionMatcher = BUKKIT_VERSION_PATTERN.matcher(Bukkit.getServer().getBukkitVersion());

        IMapping mapping = null;
        if (bukkitVersionMatcher.find()) {
            String version = bukkitVersionMatcher.group();
            logger.info("Minecraft version found: {}", version);
            mapping = NmsMappingSelector.fromMinecraftVersion(version);
        }
        return mapping;
    }

    private IMapping getMappingFromNms() {
        final Matcher nmsMatcher = NMS_PATTERN.matcher(getServer().getClass().toString());

        IMapping mapping = null;
        if (nmsMatcher.find()) {
            String version = nmsMatcher.group();
            logger.info("NMS package found: {}", version);
            mapping = NmsMappingSelector.fromNmsVersion(version);
        }
        return mapping;
    }
}
