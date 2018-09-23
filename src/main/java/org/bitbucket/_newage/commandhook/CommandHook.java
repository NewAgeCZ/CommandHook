package org.bitbucket._newage.commandhook;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.plugin.java.JavaPlugin;

public class CommandHook extends JavaPlugin {

	
	public void onEnable() {
		final Pattern p = Pattern.compile("(v\\d+_\\d+_\\w+)");
		final Matcher m = p.matcher(getServer().getClass().toString());
		String version;
		if(m.find()) {
			version = m.group();
			System.out.println("NMS package found: "+version);
			getServer().getPluginManager().registerEvents(new CommandBlockListener(new RefUtil(version)), this);
		} else {
			System.out.println("Unable to obtain NMS package");
		}
		//getServer().getLogger().info("CommandHook, server version: "+getServer().getBukkitVersion());
		//getServer().getLogger().info("Class: "+getServer().getClass());
		//getServer().getPluginManager().registerEvents(new CommandBlockListener(new RefUtil(getServer().getBukkitVersion())), this);
	}
}
