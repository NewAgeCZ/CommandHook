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
     */
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

            default:
                logger.warn("Mapping for {} not found! Either the plugin is outdated or has not been updated yet!", nmsVersion);
                logger.info("Falling back to legacy mode");
                mapping = new LegacyMapping(nmsVersion);
        }

        return mapping;
    }
}
