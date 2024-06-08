package org.bitbucket._newage.commandhook.mapping;

import org.bitbucket._newage.commandhook.legacy.mapping.LegacyMapping;
import org.bitbucket._newage.commandhook.mapping.api.IMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mapping provider static class
 */
public class NmsMappingSelector {

    private static final Logger logger = LoggerFactory.getLogger(NmsMappingSelector.class);

    private NmsMappingSelector() {

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
    public static IMapping fromNmsVersion(String nmsVersion) {
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
                logger.warn("Mapping for {} not found! Either the plugin is outdated or has not been updated yet!", nmsVersion);
                logger.info("Falling back to legacy mode");
                mapping = new LegacyMapping(nmsVersion);
        }

        return mapping;
    }

    public static IMapping fromMinecraftVersion(String minecraftVersion) {
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
            logger.warn("Mapping for {} not found! Either the plugin is outdated or has not been updated yet!", minecraftVersion);
            logger.info("Falling back to legacy mode");
            mapping = new LegacyMapping(minecraftVersion);
        }

        return mapping;
    }
}
