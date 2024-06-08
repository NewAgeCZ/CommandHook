package org.bitbucket._newage.commandhook.mapping;

import org.bitbucket._newage.commandhook.mapping.api.IMapping;
import org.bitbucket._newage.commandhook.mapping.api.MappingSelector;

public class MappingProvider {

    private MappingProvider() {}

    public static IMapping getMapping(ServerBrand serverBrand) {
        final MappingSelector selector;
        switch (serverBrand) {
        case SPIGOT:
            selector = new SpigotMappingSelector();
            break;
        case PAPER_BASED:
            selector = new PaperMappingSelector();
            break;
        default:
            throw new IllegalArgumentException("Unable to provide mappings for " +serverBrand);
        }

        return selector.getMapping();
    }
}
