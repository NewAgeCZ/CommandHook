package org.bitbucket._newage.commandhook.mapping;

import org.bitbucket._newage.commandhook.mapping.api.IMapping;
import org.bitbucket._newage.commandhook.mapping.api.MappingSelector;

class PaperMappingSelector extends MappingSelector {

    @Override
    public IMapping getMapping() {
        return new MojangMapping();
    }
}
