package org.bitbucket._newage.commandhook;

import org.bitbucket._newage.commandhook.mapping.MappingProvider;
import org.bitbucket._newage.commandhook.mapping.ServerBrand;
import org.bitbucket._newage.commandhook.mapping.api.IMapping;
import org.bitbucket._newage.commandhook.util.VersionUtil;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CommandHook extends JavaPlugin {
    private static final Logger log = LoggerFactory.getLogger(CommandHook.class);

    @Override
    public void onEnable() {
        ServerBrand serverBrand = getServerBrand();
        IMapping mapping = MappingProvider.getMapping(serverBrand);
        if (mapping == null) {
            log.error("Mapping not found, plugin will not work");
            return;
        }

        CommandBlockListener listener = new CommandBlockListener(mapping, VersionUtil.getMinecraftVersion());
        getServer().getPluginManager().registerEvents(listener, this);
    }

    protected abstract ServerBrand getServerBrand();
}
