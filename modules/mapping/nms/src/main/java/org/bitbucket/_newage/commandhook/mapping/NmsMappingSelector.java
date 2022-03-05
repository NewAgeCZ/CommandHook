package org.bitbucket._newage.commandhook.mapping;

import org.bitbucket._newage.commandhook.mapping.api.IMapping;

/**
 * Mapping provider static class
 */
public class NmsMappingSelector {

    private NmsMappingSelector() {

    }

    /**
     * Obtain mapping from given NMS version
     *
     * @param nmsVersion e.g. v1_16_R3
     * @return implementation of {@link IMapping}
     */
    public static IMapping fromNmsVersion(String nmsVersion) {
        IMapping mapping = null;
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
            default:
                mapping = new NmsV1_18_R2();
        }

        return mapping;
    }
}
