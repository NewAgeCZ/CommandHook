package org.bitbucket._newage.commandhook.legacy.mapping;

import org.bitbucket._newage.commandhook.legacy.Pre_1_17;
import org.bitbucket._newage.commandhook.legacy.V1_13;
import org.bitbucket._newage.commandhook.legacy.V1_17;
import org.bitbucket._newage.commandhook.legacy.V1_18;
import org.bitbucket._newage.commandhook.legacy.VersionMapping;

public class ReflectionMappingSelector {

    public static VersionMapping fromNmsVersion(String nmsVersion) {
        VersionMapping mapping = null;
        switch (nmsVersion) {
            case "v1_13_R1":
            case "v1_13_R2":
                mapping = new V1_13(nmsVersion);
                break;

            case "v1_14_R1":
            case "v1_15_R1":
            case "v1_16_R1":
            case "v1_16_R2":
            case "v1_16_R3":
                mapping = new Pre_1_17(nmsVersion);
                break;

            case "v1_17_R1":
                mapping = new V1_17(nmsVersion);
                break;

            case "v1_18_R1":
            default:
                mapping = new V1_18(nmsVersion);
        }

        return mapping;
    }
}
