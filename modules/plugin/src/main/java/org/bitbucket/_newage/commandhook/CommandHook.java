package org.bitbucket._newage.commandhook;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bitbucket._newage.commandhook.mapping.NmsMappingSelector;
import org.bitbucket._newage.commandhook.mapping.api.IMapping;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Plugin entry point.
 */
public class CommandHook extends JavaPlugin {
    private static final Pattern NMS_PATTERN = Pattern.compile("(v\\d+_\\d+_\\w+)");
    private final Logger logger = LoggerFactory.getLogger(CommandHook.class);

    public void onEnable() {
        final Matcher nmsMatcher = NMS_PATTERN.matcher(getServer().getClass().toString());

        String version;
        if (nmsMatcher.find()) {
            version = nmsMatcher.group();
            logger.info("NMS package found: {}", version);
            IMapping mapping = NmsMappingSelector.fromNmsVersion(version);

            getServer().getPluginManager().registerEvents(new CommandBlockListener(mapping), this);
        } else {
            logger.error("Unable to obtain NMS package, plugin will not work.");
        }
    }
}
