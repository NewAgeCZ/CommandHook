package org.bitbucket._newage.commandhook.mapping;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bitbucket._newage.commandhook.mapping.api.IMapping;
import org.bitbucket._newage.commandhook.mapping.api.MappingSelector;
import org.bukkit.Bukkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpigotMappingSelector extends MappingSelector {
    private static final Pattern NMS_PATTERN = Pattern.compile("(v\\d+_\\d+_\\w+)");
    private static final Pattern BUKKIT_VERSION_PATTERN = Pattern.compile("(\\d+\\.\\d+(\\.\\d+)?)");
    private static final Logger log = LoggerFactory.getLogger(SpigotMappingSelector.class);

    @Override
    public IMapping getMapping() {
        IMapping mapping = getMappingFromBukkitVersion();
        if (mapping == null) {
            log.warn("Unable to obtain Minecraft version, falling back to NMS parsing");
            mapping = getMappingFromNms();
        }

        if (mapping == null) {
            log.warn("Mapping not found! Either the plugin is outdated or has not been updated yet!");
        }

        return mapping;
    }

    private IMapping getMappingFromBukkitVersion() {
        final Matcher bukkitVersionMatcher = BUKKIT_VERSION_PATTERN.matcher(Bukkit.getServer().getBukkitVersion());

        IMapping mapping = null;
        if (bukkitVersionMatcher.find()) {
            String version = bukkitVersionMatcher.group();
            log.info("Minecraft version found: {}", version);
            mapping = fromMinecraftVersion(version);
        }
        return mapping;
    }

    private IMapping getMappingFromNms() {
        final Matcher nmsMatcher = NMS_PATTERN.matcher(Bukkit.getServer().getClass().toString());

        IMapping mapping = null;
        if (nmsMatcher.find()) {
            String version = nmsMatcher.group();
            log.info("NMS package found: {}", version);
            mapping = fromNmsVersion(version);
        }
        return mapping;
    }

    private static IMapping fromMinecraftVersion(String minecraftVersion) {
        IMapping mapping;
        switch (minecraftVersion) {
        case "1.13":
            mapping = new NmsV1_13_R1();
            break;

        case "1.13.1":
        case "1.13.2":
            mapping = new NmsV1_13_R2();
            break;

        case "1.14":
        case "1.14.1":
        case "1.14.2":
        case "1.14.3":
        case "1.14.4":
            mapping = new NmsV1_14();
            break;

        case "1.15":
        case "1.15.1":
        case "1.15.2":
            mapping = new NmsV1_15();
            break;

        case "1.16.1":
            mapping = new NmsV1_16_R1();
            break;

        case "1.16.2":
        case "1.16.3":
            mapping = new NmsV1_16_R2();
            break;

        case "1.16.4":
        case "1.16.5":
            mapping = new NmsV1_16_R3();
            break;

        case "1.17":
        case "1.17.1":
            mapping = new NmsV1_17();
            break;

        case "1.18":
        case "1.18.1":
            mapping = new NmsV1_18_R1();
            break;

        case "1.18.2":
            mapping = new NmsV1_18_R2();
            break;

        case "1.19":
        case "1.19.1":
        case "1.19.2":
            mapping = new NmsV1_19_R1();
            break;

        case "1.19.3":
            mapping = new NmsV1_19_R2();
            break;

        case "1.19.4":
            mapping = new NmsV1_19_R3();
            break;

        case "1.20":
        case "1.20.1":
            mapping = new NmsV1_20_R1();
            break;

        case "1.20.2":
            mapping = new NmsV1_20_R2();
            break;

        case "1.20.3":
        case "1.20.4":
            mapping = new NmsV1_20_R3();
            break;
        case "1.20.5":
        case "1.20.6":
            mapping = new NmsV1_20_R4();
            break;

        default:
            mapping = null;
        }

        return mapping;
    }

    /**
     * Obtain mapping from given NMS version
     *
     * @param nmsVersion e.g. v1_16_R3
     * @return implementation of {@link IMapping}
     * @deprecated Paper will provide only jars without relocation -> no more relocation shenanigans.
     *             Use Minecraft version instead
     */
    @Deprecated
    private static IMapping fromNmsVersion(String nmsVersion) {
        IMapping mapping;
        switch (nmsVersion) {
        case "v1_13_R1":
            mapping = new NmsV1_13_R1();
            break;

        case "v1_13_R2":
            mapping = new NmsV1_13_R2();
            break;

        case "v1_14_R1":
            mapping = new NmsV1_14();
            break;

        case "v1_15_R1":
            mapping = new NmsV1_15();
            break;

        case "v1_16_R1":
            mapping = new NmsV1_16_R1();
            break;

        case "v1_16_R2":
            mapping = new NmsV1_16_R2();
            break;

        case "v1_16_R3":
            mapping = new NmsV1_16_R3();
            break;

        case "v1_17_R1":
            mapping = new NmsV1_17();
            break;

        case "v1_18_R1":
            mapping = new NmsV1_18_R1();
            break;

        case "v1_18_R2":
            mapping = new NmsV1_18_R2();
            break;

        case "v1_19_R1":
            mapping = new NmsV1_19_R1();
            break;

        case "v1_19_R2":
            mapping = new NmsV1_19_R2();
            break;

        case "v1_19_R3":
            mapping = new NmsV1_19_R3();
            break;

        case "v1_20_R1":
            mapping = new NmsV1_20_R1();
            break;

        case "v1_20_R2":
            mapping = new NmsV1_20_R2();
            break;

        case "v1_20_R3":
            mapping = new NmsV1_20_R3();
            break;

        case "v1_20_R4":
            mapping = new NmsV1_20_R4();
            break;

        default:
            mapping = null;
        }

        return mapping;
    }
}
