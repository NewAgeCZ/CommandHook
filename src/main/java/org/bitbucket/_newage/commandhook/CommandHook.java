package org.bitbucket._newage.commandhook;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bitbucket._newage.commandhook.versions.Pre_1_17;
import org.bitbucket._newage.commandhook.versions.V1_13;
import org.bitbucket._newage.commandhook.versions.V1_17;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandHook extends JavaPlugin {

	
	public void onEnable() {
		final Pattern p = Pattern.compile("(v\\d+_\\d+_\\w+)");
		final Matcher m = p.matcher(getServer().getClass().toString());
		String version;
		if(m.find()) {
			version = m.group();
			getLogger().info("NMS package found: " + version);

			RefUtil refUtil;
			switch (version) {
				case "v1_13_R1":
				case "v1_13_R2":
					refUtil = new RefUtil(new V1_13(version));
					break;
				case "v1_14_R1":
				case "v1_15_R1":
				case "v1_16_R1":
				case "v1_16_R2":
				case "v1_16_R3":
					refUtil = new RefUtil(new Pre_1_17(version));
					break;
				default:
					refUtil = new RefUtil(new V1_17(version));

			}
			getServer().getPluginManager().registerEvents(new CommandBlockListener(refUtil), this);
		} else {
			getLogger().warning("Unable to obtain NMS package");
		}
	}
}
